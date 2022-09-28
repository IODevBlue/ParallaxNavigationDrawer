package com.blueiobase.api.android.parallaxnavigationdrawer.listener

import com.blueiobase.api.android.parallaxnavigationdrawer.ParallaxNavigationDrawer

interface OnDrawerCloseListener {

    fun onDrawerClose (
        parallaxNavigationDrawer: ParallaxNavigationDrawer,
        isLeftDrawerClose: Boolean,
        isRightDrawerClose: Boolean
    )
}