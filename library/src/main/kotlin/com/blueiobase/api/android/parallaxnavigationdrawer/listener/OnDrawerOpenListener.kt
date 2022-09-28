package com.blueiobase.api.android.parallaxnavigationdrawer.listener

import com.blueiobase.api.android.parallaxnavigationdrawer.ParallaxNavigationDrawer

interface OnDrawerOpenListener {
    fun onDrawerOpen(
        parallaxNavigationDrawer: ParallaxNavigationDrawer,
        isLeftDrawerOpen: Boolean,
        isRightDrawerOpen: Boolean
    )
}