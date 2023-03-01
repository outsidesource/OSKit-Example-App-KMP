package com.outsidesource.oskitExample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.composeUI.composeUIModule
import com.outsidesource.oskitExample.composeUI.ui.app.App

private val koin = initKoin(extraModules = composeUIModule.toTypedArray()).koin

fun main() = application {
    Window(title = "OSKit-KMP Example", onCloseRequest = ::exitApplication) {
        App()
    }
}