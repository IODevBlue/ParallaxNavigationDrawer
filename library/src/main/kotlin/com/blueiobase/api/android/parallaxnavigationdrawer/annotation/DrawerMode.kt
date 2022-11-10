package com.blueiobase.api.android.parallaxnavigationdrawer.annotation

import androidx.annotation.IntDef
import com.blueiobase.api.android.parallaxnavigationdrawer.ParallaxNavigationDrawer

@IntDef(
    ParallaxNavigationDrawer.DRAWER_MODE_LEFT,
    ParallaxNavigationDrawer.DRAWER_MODE_RIGHT,
    ParallaxNavigationDrawer.DRAWER_MODE_BOTH,
    ParallaxNavigationDrawer.DRAWER_MODE_NONE
)
@Retention(AnnotationRetention.SOURCE)
annotation class DrawerMode
