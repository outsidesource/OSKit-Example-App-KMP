package com.outsidesource.oskitExample

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.outsidesource.oskitExample.common.PlatformContext
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.file.IKMPFileHandler
import com.outsidesource.oskitkmp.file.KMPFileHandlerContext
import composeAppModule
import kotlinx.browser.document
import org.koin.core.component.inject
import ui.app.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(
        platformContext = PlatformContext,
        extraModules = composeAppModule.toTypedArray()
    ).koin

    val fileHandler by koinInjector.inject<IKMPFileHandler>()
    fileHandler.init(KMPFileHandlerContext())

    ComposeViewport(document.body!!) {
        App()
    }
}