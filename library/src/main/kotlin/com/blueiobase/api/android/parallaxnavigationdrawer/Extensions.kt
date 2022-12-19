package com.blueiobase.api.android.parallaxnavigationdrawer

import android.content.Context

/**
 * Extension function on a [Context] to create a [ParallaxNavigationDrawer].
 * @param init Initialization receiver function for the [ParallaxNavigationDrawer].
 */
fun Context.parallaxNavigationDrawer(init: ParallaxNavigationDrawer.()->Unit): ParallaxNavigationDrawer {
    val pnd = ParallaxNavigationDrawer(this)
    pnd.init()
    return pnd
}