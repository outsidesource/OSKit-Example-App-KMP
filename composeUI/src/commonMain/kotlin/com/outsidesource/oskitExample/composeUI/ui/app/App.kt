package com.outsidesource.oskitExample.composeUI.ui.app

import androidx.compose.runtime.Composable
import com.outsidesource.oskitExample.composeUI.ui.Route
import com.outsidesource.oskitExample.composeUI.ui.app.theme.AppTheme
import com.outsidesource.oskitExample.composeUI.ui.appStateExample.AppStateExampleScreen
import com.outsidesource.oskitExample.composeUI.ui.device.DeviceHomeScreen
import com.outsidesource.oskitExample.composeUI.ui.file.FileHandlingScreen
import com.outsidesource.oskitExample.composeUI.ui.home.HomeScreen
import com.outsidesource.oskitExample.composeUI.ui.images.ImagesScreen
import com.outsidesource.oskitExample.composeUI.ui.markdown.MarkdownScreen
import com.outsidesource.oskitExample.composeUI.ui.popups.PopupsScreen
import com.outsidesource.oskitExample.composeUI.ui.viewStateExample.ViewStateExampleScreen
import com.outsidesource.oskitcompose.lib.rememberInject
import com.outsidesource.oskitcompose.router.RouteSwitch
import com.outsidesource.oskitcompose.systemui.SystemBarColorEffect
import com.outsidesource.oskitcompose.systemui.SystemBarIconColor

@Composable
fun App(
    interactor: AppViewInteractor = rememberInject<AppViewInteractor>()
) {

    SystemBarColorEffect(
        statusBarIconColor = SystemBarIconColor.Light,
    )

    AppTheme {
        RouteSwitch(interactor.coordinator) {
            when (it) {
                is Route.Home -> HomeScreen()
                is Route.AppStateExample -> AppStateExampleScreen()
                is Route.ViewStateExample -> ViewStateExampleScreen(it.depth)
                is Route.DeviceHome -> DeviceHomeScreen(it.deviceId)
                is Route.FileHandling -> FileHandlingScreen()
                is Route.Images -> ImagesScreen()
                is Route.Markdown -> MarkdownScreen()
                is Route.Popups -> PopupsScreen()
            }
        }
    }
}