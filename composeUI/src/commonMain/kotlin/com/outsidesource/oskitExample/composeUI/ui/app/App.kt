package com.outsidesource.oskitExample.composeUI.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.outsidesource.oskitExample.composeUI.state.app.AppViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.Route
import com.outsidesource.oskitExample.composeUI.ui.appStateExample.AppStateExampleScreen
import com.outsidesource.oskitExample.composeUI.ui.device.DeviceHomeScreen
import com.outsidesource.oskitExample.composeUI.ui.home.HomeScreen
import com.outsidesource.oskitExample.composeUI.ui.viewStateExample.ViewStateExampleScreen
import com.outsidesource.oskitkmp.router.RouteSwitch
import org.koin.java.KoinJavaComponent.inject

@Composable
fun App(
    interactor: AppViewInteractor = remember { inject<AppViewInteractor>(AppViewInteractor::class.java).value }
) {
    RouteSwitch(interactor.coordinator) {
        when (it) {
            is Route.Home -> HomeScreen()
            is Route.AppStateExample -> AppStateExampleScreen()
            is Route.ViewStateExample -> ViewStateExampleScreen(it.depth)
            is Route.DeviceHome -> DeviceHomeScreen(it.deviceId)
        }
    }
}