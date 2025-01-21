package com.outsidesource.oskitExample

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.outsidesource.oskitExample.common.PlatformContext
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.KmpCapabilityContext
import com.outsidesource.oskitkmp.filesystem.IKmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
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

    KmpFs.init(KmpFsContext())

    val capabilities by koinInjector.inject<KmpCapabilities>()
    capabilities.init(KmpCapabilityContext())

    ComposeViewport(document.body!!) {
        App()
    }
}