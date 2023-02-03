package com.outsidesource.oskitExample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.outsidesource.oskitExample.composeUI.ui.app.App


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
