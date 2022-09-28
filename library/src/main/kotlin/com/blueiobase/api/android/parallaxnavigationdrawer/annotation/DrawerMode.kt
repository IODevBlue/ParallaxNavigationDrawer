package com.blueiobase.api.android.parallaxnavigationdrawer.annotation

import androidx.annotation.IntDef
import com.blueiobase.api.android.parallaxnavigationdrawer.base.IParallaxDrawer

@IntDef(
    IParallaxDrawer.DRAWER_MODE_LEFT,
    IParallaxDrawer.DRAWER_MODE_RIGHT,
    IParallaxDrawer.DRAWER_MODE_BOTH,
    IParallaxDrawer.DRAWER_MODE_NONE
)
@Retention(AnnotationRetention.SOURCE)
annotation class DrawerMode
