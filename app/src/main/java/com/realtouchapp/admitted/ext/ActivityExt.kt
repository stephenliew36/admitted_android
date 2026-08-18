package com.realtouchapp.admitted.ext

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.WindowInsets
import androidx.core.view.WindowInsetsControllerCompat

// https://stackoverflow.com/a/79338465
fun Activity.configureEdgeToEdge(
    backgroundColor: Int = Color.WHITE,
    isDarkStatusBar: Boolean = true,
    isDarkNavigationBar: Boolean = true
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
            view.setBackgroundColor(backgroundColor)
            setStatusBarStyle(isDarkStatusBar)
            setNavigationBarStyle(isDarkNavigationBar)

            // Adjust padding to avoid overlap
            view.setPadding(0, statusBarInsets.top, 0, statusBarInsets.bottom)
            insets
        }
    }
}

fun Activity.setStatusBarStyle(isDarkStatusBar: Boolean) {
    val insetsController = WindowInsetsControllerCompat(window, window.decorView)
    insetsController.isAppearanceLightStatusBars = isDarkStatusBar
}

fun Activity.setNavigationBarStyle(isDarkNavigationBar: Boolean) {
    val insetsController = WindowInsetsControllerCompat(window, window.decorView)
    insetsController.isAppearanceLightNavigationBars = isDarkNavigationBar
}