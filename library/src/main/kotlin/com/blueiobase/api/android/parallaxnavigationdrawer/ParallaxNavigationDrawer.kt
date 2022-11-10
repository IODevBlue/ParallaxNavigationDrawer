package com.blueiobase.api.android.parallaxnavigationdrawer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 *
 * @author IO DevBlue
 * @since v1.0.0
 */
class ParallaxNavigationDrawer @JvmOverloads constructor(
    mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): ViewGroup(mContext, attrs, defStyleAttr) {

    companion object {

        /** Constant indicating that the [ParallaxNavigationDrawer] has no drawers. */
        const val DRAWER_MODE_NONE = 0

        /** Constant indicating that the [ParallaxNavigationDrawer] has only a left drawer. */
        const val DRAWER_MODE_LEFT = 1

        /** Constant indicating that the [ParallaxNavigationDrawer] has only a right drawer. */
        const val DRAWER_MODE_RIGHT = 2

        /** Constant indicating that the [ParallaxNavigationDrawer] has both left and right drawers. */
        const val DRAWER_MODE_BOTH = 3
    }

    /** The [View] contained in the left drawer. */
    var leftDrawerView: View? = null

    /** The [View] contained in the right drawer. */
    var rightDrawerView: View? = null

    /** The main content [View]. */
    val mainContentView: View? = null

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        TODO("Not yet implemented")
    }
}