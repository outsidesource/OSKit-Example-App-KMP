package com.outsidesource.oskitExample.ui.app

import androidx.compose.runtime.Composable
import com.outsidesource.oskitExample.ui.Route
import com.outsidesource.oskitExample.ui.app.theme.AppTheme
import com.outsidesource.oskitExample.ui.app.theme.DarkAppColors
import com.outsidesource.oskitExample.ui.app.theme.LightAppColors
import com.outsidesource.oskitExample.ui.appStateExample.AppStateExampleScreen
import com.outsidesource.oskitExample.ui.device.DeviceHomeScreen
import com.outsidesource.oskitExample.ui.file.FileHandlingScreen
import com.outsidesource.oskitExample.ui.home.HomeScreen
import com.outsidesource.oskitExample.ui.images.ResourcesScreen
import com.outsidesource.oskitExample.ui.iosServices.IOSServicesScreen
import com.outsidesource.oskitExample.ui.markdown.MarkdownScreen
import com.outsidesource.oskitExample.ui.popups.PopupsScreen
import com.outsidesource.oskitExample.ui.viewStateExample.ViewStateExampleScreen
import com.outsidesource.oskitExample.ui.widgets.WidgetsScreen
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInject
import com.outsidesource.oskitcompose.router.RouteSwitch
import com.outsidesource.oskitcompose.systemui.SystemBarColorEffect
import com.outsidesource.oskitcompose.systemui.SystemBarIconColor

@Composable
fun App(
    interactor: AppViewInteractor = rememberInject<AppViewInteractor>()
) {
    val state = interactor.collectAsState()

    SystemBarColorEffect(
        statusBarIconColor = SystemBarIconColor.Light,
    )

    AppTheme(
        colorsOverride = if (state.isDarkTheme) DarkAppColors else LightAppColors
    ) {
        RouteSwitch(interactor.coordinator) {
            when (it) {
                is Route.Home -> HomeScreen()
                is Route.AppStateExample -> AppStateExampleScreen()
                is Route.ViewStateExample -> ViewStateExampleScreen(it.depth)
                is Route.DeviceHome -> DeviceHomeScreen(it.deviceId)
                is Route.FileHandling -> FileHandlingScreen()
                is Route.Resources -> ResourcesScreen()
                is Route.Markdown -> MarkdownScreen()
                is Route.Popups -> PopupsScreen()
                is Route.IOSServices -> IOSServicesScreen()
                is Route.Widgets -> WidgetsScreen()
            }
        }
    }
}