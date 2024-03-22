package com.blueiobase.api.android.parallaxnavigationdrawer.util

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import com.blueiobase.api.android.parallaxnavigationdrawer.ParallaxNavigationDrawer

/**
 * Utility class for the [ParallaxNavigationDrawer] library.
 * @author IO DevBlue
 * @since 1.0.0
 */
internal object Util {

    /**
     * Returns the width of the screen.
     * @param context The current [Context].
     * @return The width of the screen.
     */
    @Suppress("DEPRECATION")
    fun screenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val metrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(metrics)
            metrics.widthPixels
        }
    }

    /**
     * Returns the height of the screen.
     * @param context The current [Context].
     * @return The height of the screen.
     */
    @Suppress("DEPRECATION")
    fun screenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val metrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(metrics)
            metrics.heightPixels
        }
    }

}