package com.tuskanddot.tuskanddotproject

import androidx.compose.ui.window.ComposeUIViewController
import com.tuskanddot.tuskanddotproject.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) {
    App()
}