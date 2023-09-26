package com.outsidesource.oskitExample

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.composeUI.composeUIModule
import com.outsidesource.oskitExample.composeUI.ui.app.App
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitcompose.window.SizedWindow
import com.outsidesource.oskitcompose.window.rememberPersistedWindowState
import com.outsidesource.oskitkmp.file.DesktopKMPFileHandler
import com.outsidesource.oskitkmp.file.IKMPFileHandler
import com.outsidesource.oskitkmp.file.KMPFileHandlerContext
import org.koin.core.component.inject
import java.awt.Dimension

private val koin = initKoin(extraModules = composeUIModule.toTypedArray()).koin

fun main() = application {
    val fileHandler by koinInjector.inject<IKMPFileHandler>()
    val windowState = rememberPersistedWindowState("OSKit-KMP-Example", initialSize = Dimension(800, 600))

    SizedWindow(
        title = "OSKit-KMP Example",
        onCloseRequest = ::exitApplication,
        minWindowSize = Dimension(800, 600),
        state = windowState,
    ) {
        DisposableEffect(Unit) {
            fileHandler.init(KMPFileHandlerContext(this@SizedWindow.window))
            onDispose {  }
        }

        App()
    }
}