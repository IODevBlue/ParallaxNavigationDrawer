package com.blueiobase.api.android.parallaxnavigationdrawer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.blueiobase.api.android.parallaxnavigationdrawer.annotation.DrawerMode
import com.blueiobase.api.android.parallaxnavigationdrawer.util.Util
import kotlin.math.abs

/**
 * This is a Navigation Drawer that supports sliding from the left and right ends of the screen with parallax effect.
 * @author IO DevBlue
 * @since 1.0.0
 */
class ParallaxNavigationDrawer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        /** Constant indicating that the [ParallaxNavigationDrawer] has no drawers. */
        const val DRAWER_MODE_NONE = 0
        /** Constant indicating that the [ParallaxNavigationDrawer] has only a left drawer. */
        const val DRAWER_MODE_LEFT = 1
        /** Constant indicating that the [ParallaxNavigationDrawer] has only a right drawer. */
        const val DRAWER_MODE_RIGHT = 2
        /** Constant indicating that the [ParallaxNavigationDrawer] has both left and right drawers. */
        const val DRAWER_MODE_BOTH = 3

        /** The default value for the [duration]. */
        private const val DEFAULT_DURATION = 700
    }

    /**
     * Interface to listen to changes in the open and close states of both the left and right drawers simultaneously.
     * @see OnLeftDrawerStateChangedListener
     * @see OnRightDrawerStateChangedListener
     */
    interface OnDrawerStateChangedListener {
        /**
         * Callback invoked when drawer state changes for both the left or right drawer.
         * @param isLeftDrawerOpen `true` if left drawer is open, `false` if otherwise.
         * @param isRightDrawerOpen `true` if right drawer is open, `false` if otherwise.
         */
        fun onDrawerStateChanged(isLeftDrawerOpen: Boolean, isRightDrawerOpen: Boolean)
    }

    /**
     * Interface to listen to changes in the open and close state of the left drawer.
     * @see OnDrawerStateChangedListener
     * @see OnRightDrawerStateChangedListener
     */
    interface OnLeftDrawerStateChangedListener {
        /**
         * Callback invoked when drawer state changes for the left drawer.
         * @param isOpen `true` if left drawer is open, `false` if otherwise.
         */
        fun onDrawerStateChanged(isOpen: Boolean)
    }

    /**
     * Interface to listen to changes in the open and close state of the right drawer.
     * @see OnDrawerStateChangedListener
     * @see OnLeftDrawerStateChangedListener
     */
    interface OnRightDrawerStateChangedListener {
        /**
         * Callback invoked when drawer state changes for the right drawer.
         * @param isOpen `true` if right drawer is open, `false` if otherwise.
         */
        fun onDrawerStateChanged(isOpen: Boolean)
    }

    /** The [View] representing the left drawer. */
    var leftDrawerView: View? = null
        private set

    /** The [View] representing the right drawer. */
    var rightDrawerView: View? = null
        private set

    /** The main content [View] which can slide left to reveal the [leftDrawerView] or slide right to reveal the [rightDrawerView]. */
    var mainContentView: View? = null
        private set

    /** The current [DrawerMode]. */
    @DrawerMode
    var drawerMode = DRAWER_MODE_NONE
        set(value) {
            when (value) {
                DRAWER_MODE_LEFT -> closeRightDrawer()
                DRAWER_MODE_RIGHT -> closeLeftDrawer()
                DRAWER_MODE_NONE -> {
                    closeLeftDrawer()
                    closeRightDrawer()
                }
            }
            field = value
        }

    /** The padding applied to the [mainContentView] when the [ParallaxNavigationDrawer] is open. */
    var mainContentPadding = 0
    /** The time it takes for the [ParallaxNavigationDrawer] to complete its open and close animation. */
    var duration = 0
    /** Enables the parallax feature for the [ParallaxNavigationDrawer]. */
    var parallax = false

    /**
     * The alpha value of the shadow applied to the [mainContentView] when the [ParallaxNavigationDrawer] is open.
     * @see mainContentShadowColor
     */
    @FloatRange(from = 0.0, to = 1.0)
    var mainContentShadowAlpha = 0F

    /**
     * The color of the shadow applied to the [mainContentView] when the [ParallaxNavigationDrawer] is open.
     *
     * Default is **BLACK**.
     * @see mainContentShadowAlpha
     */
    @ColorInt
    var mainContentShadowColor = 0
        set(value) {
            field = value
            mainContentShadowPaint.color = value
            postInvalidate()
        }

    /** Enables the close-on-touch feature which closes the [ParallaxNavigationDrawer] when the [mainContentView] is touched. */
    var mainContentCloseOnTouch = false

    /** Enables swiping and dragging on the [mainContentView] to reveal either the [leftDrawerView] or [rightDrawerView]. */
    var enableSwiping = false

    /** Listener for the open and close states for both the left and right drawers. */
    var onDrawerStateChangedListener: OnDrawerStateChangedListener? = null
    /** Listener for the open and close states of the left drawer. */
    var onLeftDrawerStateChangedListener: OnLeftDrawerStateChangedListener? = null
    /** Listener for the open and close states of the right drawer. */
    var onRightDrawerStateChangedListener: OnRightDrawerStateChangedListener? = null

    /** The width of the screen. */
    private val screenWidth = Util.screenWidth(context)
    /** The height of the screen. */
    private val screenHeight = Util.screenHeight(context)
    /** The maximum width allowed to swipe the [mainContentView]. */
    private var maxSwipeWidth = 0
    /** The width occupied by the [mainContentView]. */
    private var mainContentWidth = 0
    /** The height occupied by the [mainContentView]. */
    private var mainContentHeight = 0
    /** Holds the last known X position during an [onTouchEvent] */
    private var mLastX = 0
    /** Holds the last known X position during an [onInterceptTouchEvent].*/
    private var mLastXIntercept  = 0
    /** Holds the last known Y position during an [onInterceptTouchEvent]. */
    private var mLastYIntercept = 0
    /** The difference between the current X position during a touch event and the [mLastX]. */
    private var mDx = 0
    /** [Boolean] to validate if the [leftDrawerView] is open. */
    private var isLeftDrawerOpen = false
    /** [Boolean] to validate if the [rightDrawerView] is open. */
    private var isRightDrawerOpen = false
    /** Control variable to store the currently open drawer. `L`- Left, `R`- Right and `N`- None. */
    private var whichOpen = 'N'

    /** The [Scroller] needed to perform the swiping. */
    private val mScroller = Scroller(context)
    /** The [Paint] object used to draw the [mainContentShadowColor]. */
    private val mainContentShadowPaint by lazy {
        Paint().apply {
            color = mainContentShadowColor
            style = Paint.Style.FILL
        }
    }

    init {
        initAttrs(attrs)
    }

    override fun addView(child: View) {
        validateChildCount()
        super.addView(child)
    }

    override fun addView(child: View, index: Int) {
        validateChildCount()
        super.addView(child, index)
    }

    override fun addView(child: View, params: LayoutParams) {
        validateChildCount()
        super.addView(child, params)
    }

    override fun addView(child: View, index: Int, params: LayoutParams) {
        validateChildCount()
        super.addView(child, index, params)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthResult = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            else  -> screenWidth
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightResult = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            else -> screenHeight
        }
        initParallaxNavigationDrawer(widthResult, heightResult)
        measureChildView(mainContentView, widthMeasureSpec, heightMeasureSpec)
        measureChildView(leftDrawerView, widthMeasureSpec, heightMeasureSpec)
        measureChildView(rightDrawerView, widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mainContentWidth, mainContentHeight)
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        val result = super.drawChild(canvas, child, drawingTime)
        if (child === mainContentView) {
            mainContentView?.let {
                val rX = abs(scrollX)
                var alpha = if (rX == 0) {
                    0
                } else if (rX >= maxSwipeWidth) {
                    ((1.0f - mainContentShadowAlpha) * 255).toInt()
                } else {
                    (abs((1.0f - mainContentShadowAlpha) / maxSwipeWidth) * rX * 255).toInt()
                }
                alpha = if (alpha < 0 || alpha > 255) 255 else alpha
                mainContentShadowPaint.alpha = alpha
                canvas.drawRect(it.left.toFloat(), it.top.toFloat(), it.right.toFloat(), it.bottom.toFloat(), mainContentShadowPaint)
            }
        }
        return result
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        leftDrawerView?.layout(-maxSwipeWidth, 0, 0, mainContentHeight)
        rightDrawerView?.layout(mainContentWidth, 0, mainContentWidth + maxSwipeWidth, mainContentHeight)
        mainContentView?.layout(0, 0, mainContentWidth, mainContentHeight)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            leftDrawerView?.let { leftDrawerParallaxOpen() }
            rightDrawerView?.let { rightDrawerParallaxOpen() }
            postInvalidate()
            invalidateContent()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> intercept = false
            MotionEvent.ACTION_MOVE -> {
                val deltaX = ev.x.toInt() - mLastXIntercept
                val deltaY = ev.y.toInt() - mLastYIntercept
                if (abs(deltaX) > abs(deltaY)) {
                    if (isLeftDrawerOpen) {
                        intercept = x > maxSwipeWidth
                    } else if (isRightDrawerOpen) {
                        intercept = x < mainContentPadding
                    } else {
                        when(drawerMode) {
                            DRAWER_MODE_LEFT -> { if (deltaX > 0) intercept = x <= mainContentPadding / 2 }
                            DRAWER_MODE_RIGHT -> { if(deltaX < 0) intercept = x >= maxSwipeWidth - mainContentPadding / 2 }
                            DRAWER_MODE_BOTH -> {
                                if (deltaX > 0) intercept = x <= mainContentPadding / 2
                                if (deltaX < 0) intercept = x >= maxSwipeWidth - mainContentPadding / 2
                            }
                            else -> intercept = false
                        }
                    }
                } else intercept = false
            }
            MotionEvent.ACTION_UP -> intercept = mainContentCloseOnTouchFunc()
        }
        mLastX = x
        mLastXIntercept = x
        mLastYIntercept = y
        return intercept
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!enableSwiping) return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mLastX = event.x.toInt()
            MotionEvent.ACTION_MOVE -> {
                val currentX = event.x.toInt()
                val dx = currentX - mLastX
                if (dx < 0) scrollLeft(dx)
                else scrollRight(dx)
                mLastX = currentX
                mDx = dx
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (mDx > 0) {
                    springMotionScrollRight()
                } else {
                    springMotionScrollLeft()
                }
                mDx = 0
            }
        }
        return true
    }

    /**
     * A default `onBackPressed()` operation which closes either the left or right drawer and returns a [Boolean]
     * indicating the closed state of both drawers.
     * @return `true` if both left and right drawers are closed, `false` if either drawer was open.
     */
    fun onBackPressed(): Boolean {
        return if (isLeftDrawerOpen) {
            closeLeftDrawer()
            false
        } else if(isRightDrawerOpen) {
            closeRightDrawer()
            false
        } else true
    }

    /**
     * Sets the [onDrawerStateChangedListener].
     * @param init A receiver function to receive results of listener callbacks
     * @see setOnLeftDrawerStateChangedListener
     * @see setOnRightDrawerStateChangedListener
     */
    fun setOnDrawerStateChangedListener( init: (left: Boolean, right: Boolean) -> Unit) {
        val x = object: OnDrawerStateChangedListener {
            override fun onDrawerStateChanged(isLeftDrawerOpen: Boolean, isRightDrawerOpen: Boolean) {
                init(isLeftDrawerOpen, isRightDrawerOpen)
            }
        }
        onDrawerStateChangedListener = x
    }

    /** Opens or closes the [leftDrawerView] depending on its current state. */
    fun toggleLeftDrawer() {
        if (isLeftDrawerOpen) closeLeftDrawer()
        else openLeftDrawer()
    }

    /** Opens the [leftDrawerView]. */
    fun openLeftDrawer() {
        if (drawerMode == DRAWER_MODE_RIGHT || drawerMode == DRAWER_MODE_NONE) return
        isLeftDrawerOpen = true
        scrollDest(-maxSwipeWidth)
    }

    /** Closes the [leftDrawerView]. */
    fun closeLeftDrawer() {
        if (drawerMode == DRAWER_MODE_RIGHT || drawerMode == DRAWER_MODE_NONE) return
        isLeftDrawerOpen = false
        scrollDest(0)
    }

    /** Opens or closes the [rightDrawerView] depending on its current state. */
    fun toggleRightDrawer() {
        if (isRightDrawerOpen) closeRightDrawer()
        else openRightDrawer()
    }

    /** Opens the [rightDrawerView]. */
    fun openRightDrawer() {
        if (drawerMode == DRAWER_MODE_LEFT || drawerMode == DRAWER_MODE_NONE) return
        isRightDrawerOpen = true
        scrollDest(maxSwipeWidth)
    }

    /** Closes the [rightDrawerView]. */
    fun closeRightDrawer() {
        if (drawerMode == DRAWER_MODE_LEFT || drawerMode == DRAWER_MODE_NONE) return
        isRightDrawerOpen = false
        scrollDest(0)
    }

    /**
     * Sets the [OnLeftDrawerStateChangedListener].
     * @param init A receiver function to receive results of listener callbacks
     * @see setOnDrawerStateChangedListener
     * @see setOnRightDrawerStateChangedListener
     */
    fun setOnLeftDrawerStateChangedListener(init: (isOpen: Boolean) -> Unit) {
        val x = object: OnLeftDrawerStateChangedListener {
            override fun onDrawerStateChanged(isOpen: Boolean) {
                init(isOpen)
            }
        }
        onLeftDrawerStateChangedListener = x
    }

    /**
     * Sets the [OnRightDrawerStateChangedListener].
     * @param init A receiver function to receive results of listener callbacks
     * @see setOnDrawerStateChangedListener
     * @see setOnLeftDrawerStateChangedListener
     */
    fun setOnRightDrawerStateChangedListener(init: (isOpen: Boolean) -> Unit) {
        val x = object: OnRightDrawerStateChangedListener {
            override fun onDrawerStateChanged(isOpen: Boolean) {
                init(isOpen)
            }
        }
        onRightDrawerStateChangedListener = x
    }

    /**
     * Internal function to initialize the [ParallaxNavigationDrawer] using the provided [AttributeSet].
     * @param attrs [AttributeSet] configurations for this [ParallaxNavigationDrawer].
     */
    private fun initAttrs(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.ParallaxNavigationDrawer).apply {
            drawerMode = getInteger(R.styleable.ParallaxNavigationDrawer_drawerMode, DRAWER_MODE_NONE)
            mainContentPadding = getDimension(R.styleable.ParallaxNavigationDrawer_mainContentPadding, (screenWidth / 4).toFloat()).toInt()
            duration = getInteger(R.styleable.ParallaxNavigationDrawer_duration, DEFAULT_DURATION)
            parallax = getBoolean(R.styleable.ParallaxNavigationDrawer_parallax, true)
            mainContentShadowAlpha = getFloat(R.styleable.ParallaxNavigationDrawer_mainContentShadowAlpha, 0.5f)
            mainContentShadowColor = getColor(R.styleable.ParallaxNavigationDrawer_mainContentShadowColor, Color.BLACK)
            mainContentCloseOnTouch = getBoolean(R.styleable.ParallaxNavigationDrawer_mainContentCloseOnTouch, false)
            enableSwiping = getBoolean(R.styleable.ParallaxNavigationDrawer_enableSwiping, true)
            recycle()
        }
    }

    /**
     * Internal function to verify the number of child [View] objects this [ParallaxNavigationDrawer] hosts.
     *
     * **NOTE:** Only a maximum of **3** [View] objects are allowed in a [ParallaxNavigationDrawer].
     * Namely: [mainContentView], [leftDrawerView] and [rightDrawerView].
     * @throws IllegalStateException When [View] number count is more than 3.
     */
    private fun validateChildCount() {
        check(childCount <= 3) { "ParallaxNavigationDrawer cannot host more than three child views." }
    }

    /**
     * Internal function to evaluate the width and height of each child [View].
     * @param childView This represents either the [mainContentView], [leftDrawerView] or [rightDrawerView].
     * @param widthMeasureSpec The width of this [ParallaxNavigationDrawer]
     * @param heightMeasureSpec The height of this [ParallaxNavigationDrawer]
     */
    private fun measureChildView(childView: View?, widthMeasureSpec: Int, heightMeasureSpec: Int) {
        childView?.let {
            val lp: LayoutParams = it.layoutParams
            val childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, lp.width)
            val childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, lp.height)
            it.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }
    }

    /**
     * Internal function to initialize the [ParallaxNavigationDrawer].
     * @param widthResult Maximum width of the [mainContentView]
     * @param heightResult Maximum height of this [mainContentView]
     */
    private fun initParallaxNavigationDrawer(widthResult: Int, heightResult: Int) {
        check(childCount != 0) { "ParallaxNavigationDrawer must host at least one child which is the main content view. " }
        maxSwipeWidth = widthResult - mainContentPadding
        mainContentWidth = widthResult
        mainContentHeight = heightResult
        when (childCount) {
            1 -> {
                drawerMode = DRAWER_MODE_NONE
                mainContentView = getChildAt(0)
            }
            2 ->
                when(drawerMode) {
                    DRAWER_MODE_LEFT -> {
                        leftDrawerView = getChildAt(0)
                        mainContentView = getChildAt(1)
                    }
                    DRAWER_MODE_RIGHT -> {
                        rightDrawerView = getChildAt(0)
                        mainContentView = getChildAt(1)
                    }
                    else -> throw IllegalStateException(
                        "ParallaxNavigationDrawer must host only three direct child views when drawerMode is DRAWER_MODE_BOTH")
                }
            3 -> {
                leftDrawerView = getChildAt(0)
                rightDrawerView = getChildAt(1)
                mainContentView = getChildAt(2)
            }
        }
        leftDrawerView?.layoutParams?.width = maxSwipeWidth
        rightDrawerView?.layoutParams?.width = maxSwipeWidth
        val contentParams = mainContentView?.layoutParams
        contentParams?.apply {
            width = widthResult
            height = heightResult
        }
    }

    /** Redraws and recalculates the drawer. */
    private fun invalidateContent() {
        postInvalidate()
    }

    /** Animates the parallax effect when the [rightDrawerView] opens. */
    private fun rightDrawerParallaxOpen() {
        if (!parallax) return
        var rightTranslationX = 2 * (-maxSwipeWidth + scrollX) / 3
        if (scrollX == 0 || scrollX == maxSwipeWidth || scrollX == -maxSwipeWidth) {
            rightTranslationX = 0
        }
        rightDrawerView?.translationX = rightTranslationX.toFloat()
    }

    /** Animates the parallax effect when the [leftDrawerView] opens. */
    private fun leftDrawerParallaxOpen() {
        if (!parallax) return
        var leftTranslationX = 2 * (maxSwipeWidth + scrollX) / 3
        if (scrollX == 0 || scrollX >= maxSwipeWidth || scrollX <= -maxSwipeWidth) {
            leftTranslationX = 0
        }
        leftDrawerView?.translationX = leftTranslationX.toFloat()
    }

    /** Function to execute the [mainContentCloseOnTouch]. */
    private fun mainContentCloseOnTouchFunc(): Boolean {
        if (!enableSwiping) return false
        if (abs(scrollX) < maxSwipeWidth) return false
        val dX = scrollX
        if (dX < 0) {
            if (mLastX > maxSwipeWidth) {
                closeLeftDrawer()
            } else {
                return false
            }
        } else if (dX > 0) {
            if (mLastX < mainContentWidth - maxSwipeWidth) {
                closeRightDrawer()
            } else {
                return false
            }
        }
        return true
    }

    /**
     * Internal function which uses the [mScroller] to move the [ParallaxNavigationDrawer] to these coordinates.
     * @param destX The x coordinates.
     */
    private fun scrollDest(destX: Int) {
        val scrollX = scrollX
        val deltaX = destX - scrollX
        val time = abs(deltaX * 1.0f / (maxSwipeWidth * 1.0f / duration)).toInt()
        mScroller.startScroll(scrollX, 0, deltaX, 0, time)
        invalidate()
        when(destX) {
            -maxSwipeWidth -> {
                onDrawerStateChangedListener?.onDrawerStateChanged(isLeftDrawerOpen = true, isRightDrawerOpen = false)
                onLeftDrawerStateChangedListener?.onDrawerStateChanged(true)
                whichOpen = 'L'
            }
            0 -> {
                onDrawerStateChangedListener?.onDrawerStateChanged(isLeftDrawerOpen = false, isRightDrawerOpen = false)
                when (whichOpen) {
                    'L' -> onLeftDrawerStateChangedListener?.onDrawerStateChanged(false)
                    'R' -> onRightDrawerStateChangedListener?.onDrawerStateChanged(false)
                }
                whichOpen = 'N'
            }
            maxSwipeWidth -> {
                onDrawerStateChangedListener?.onDrawerStateChanged( isLeftDrawerOpen = false, isRightDrawerOpen = true)
                onRightDrawerStateChangedListener?.onDrawerStateChanged(true)
                whichOpen = 'R'
            }
        }
    }

    /** Internal function to open or close the left or right drawers automatically when there is an incomplete [MotionEvent] moving to the left. */
    private fun springMotionScrollLeft() {
        if (drawerMode == DRAWER_MODE_RIGHT) {
            if (scrollX >= maxSwipeWidth / 2) {
                openRightDrawer()
            } else {
                if (!isRightDrawerOpen) {
                    closeRightDrawer()
                }
            }
        } else if (drawerMode == DRAWER_MODE_LEFT) {
            if (-scrollX <= maxSwipeWidth / 2) {
                closeLeftDrawer()
            } else {
                if (isLeftDrawerOpen) {
                    openLeftDrawer()
                }
            }
        } else if (drawerMode == DRAWER_MODE_BOTH) {
            if (!isLeftDrawerOpen && !isRightDrawerOpen) {
                if (scrollX >= maxSwipeWidth / 2) {
                    openRightDrawer()
                } else {
                    closeRightDrawer()
                }
            } else if (isLeftDrawerOpen || scrollX < 0) {
                if (-scrollX <= maxSwipeWidth / 2) {
                    closeLeftDrawer()
                } else {
                    openLeftDrawer()
                }
            }
        }
    }

    /** Internal function to open or close the left or right drawers automatically when there is an incomplete [MotionEvent] moving to the right. */
    private fun springMotionScrollRight() {
        if (drawerMode == DRAWER_MODE_LEFT) {
            if (-scrollX >= maxSwipeWidth / 2) {
                openLeftDrawer()
            } else {
                if (!isLeftDrawerOpen) {
                    closeLeftDrawer()
                }
            }
        } else if (drawerMode == DRAWER_MODE_RIGHT) {
            if (scrollX <= maxSwipeWidth / 2) {
                closeRightDrawer()
            } else {
                if (isRightDrawerOpen) {
                    openRightDrawer()
                }
            }
        } else if (drawerMode == DRAWER_MODE_BOTH) {
            if (!isLeftDrawerOpen && !isRightDrawerOpen) {
                if (-scrollX >= maxSwipeWidth / 2) {
                    openLeftDrawer()
                } else {
                    closeLeftDrawer()
                }
            } else if (isRightDrawerOpen || scrollX > 0) {
                if (scrollX <= maxSwipeWidth / 2) {
                    closeRightDrawer()
                } else {
                    openRightDrawer()
                }
            }
        }
    }

    /**
     * Internal function to move the [mainContentView] leftward which would either close the [leftDrawerView] or open the [rightDrawerView]
     * @param dx The distance to move the [mainContentView] left horizontally.
     */
    private fun scrollLeft(dx: Int) {
        when (drawerMode) {
            DRAWER_MODE_RIGHT -> {
                if (isRightDrawerOpen || scrollX - dx >= maxSwipeWidth) {
                    openRightDrawer()
                    return
                }
                rightDrawerParallaxOpen()
            }
            DRAWER_MODE_LEFT -> {
                if (!isLeftDrawerOpen || scrollX - dx >= 0) {
                    closeLeftDrawer()
                    return
                }
                leftDrawerParallaxOpen()
            }
            DRAWER_MODE_BOTH -> {
                if (isRightDrawerOpen || scrollX - dx >= maxSwipeWidth) {
                    openRightDrawer()
                    return
                }
                leftDrawerParallaxOpen()
                rightDrawerParallaxOpen()
            }
        }
        scrollBy(-dx, 0)
        invalidateContent()
    }

    /**
     * Internal function to move the [mainContentView] rightward which would either open the [leftDrawerView] or close the [rightDrawerView]
     * @param dx The distance to move the [mainContentView] right horizontally.
     */
    private fun scrollRight(dx: Int) {
        when (drawerMode) {
           DRAWER_MODE_LEFT -> {
                if (isLeftDrawerOpen || scrollX - dx <= -maxSwipeWidth) {
                    openLeftDrawer()
                    return
                }
                leftDrawerParallaxOpen()
            }
            DRAWER_MODE_RIGHT -> {
                if (!isRightDrawerOpen || scrollX - dx <= 0) {
                    closeRightDrawer()
                    return
                }
                rightDrawerParallaxOpen()
            }
            DRAWER_MODE_BOTH -> {
                if (isLeftDrawerOpen || scrollX - dx <= -maxSwipeWidth) {
                    openLeftDrawer()
                    return
                }
                leftDrawerParallaxOpen()
                rightDrawerParallaxOpen()
            }
        }
        scrollBy(-dx, 0)
        invalidateContent()
    }
}