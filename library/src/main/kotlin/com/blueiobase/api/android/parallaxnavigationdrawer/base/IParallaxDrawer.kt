package com.blueiobase.api.android.parallaxnavigationdrawer.base

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import com.blueiobase.api.android.parallaxnavigationdrawer.annotation.DrawerMode
import com.blueiobase.api.android.parallaxnavigationdrawer.listener.OnDrawerCloseListener
import com.blueiobase.api.android.parallaxnavigationdrawer.listener.OnDrawerOpenListener


interface IParallaxDrawer {


    /**
     *
     */
    fun setDrawerMode(@DrawerMode slideMode: Int)

    /**
     *
     *
     * @param slidePadding
     */
    fun setDrawerPadding(slidePadding: Int)

    /**
     *
     *
     * @param drawerDuration
     */
    fun setDrawerAnimationDuration(drawerDuration: Int)

    /**
     *
     *
     * @param parallax
     */
    fun setParallaxEnabled(parallax: Boolean)

    /**
     *
     *
     */
    fun setMainContentAlpha(@FloatRange(from = 0.0, to = 1.0) contentAlpha: Float)

    /**
     *
     *
     * @param color
     */
    fun setMainContentShadowColor(@ColorRes color: Int)

    /**
     *
     *
     */
    fun setMainContentCloseOnTouch(canCloseFromMainContent: Boolean)

    /**
     *
     *
     */
    fun setAllowDragging(allowDragging: Boolean)

    /**
     *
     *
     * @return [View]
     */
    fun getLeftDrawerView(): View?

    /**
     *
     *
     * @return [View]
     */
    fun getRightDrawerView(): View?

    /**
     *
     *
     * @return [View]
     */
    fun getMainContentView(): View?

    fun toggleLeftDrawer()

    fun openLeftDrawer()

    fun closeLeftDrawer()

    fun isLeftDrawerOpen(): Boolean

    fun toggleRightDrawer()

    fun openRightDrawer()

    fun closeRightDrawer()

    fun isRightDrawerOpen(): Boolean

    fun setOnDrawerOpenListener(listener: OnDrawerOpenListener?)

    fun setOnDrawerCloseListener(listener: OnDrawerCloseListener?)

}