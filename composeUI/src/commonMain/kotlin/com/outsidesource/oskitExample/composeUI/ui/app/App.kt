package com.outsidesource.oskitExample.composeUI.ui.app

import androidx.compose.runtime.Composable
import com.outsidesource.oskitExample.composeUI.state.app.AppViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.Route
import com.outsidesource.oskitExample.composeUI.ui.home.HomeScreen
import com.outsidesource.oskitkmp.interactor.rememberInteractor
import com.outsidesource.oskitkmp.router.RouteSwitch
import org.koin.java.KoinJavaComponent.inject

@Composable
fun App() {
    val (state, interactor) = rememberInteractor { inject<AppViewInteractor>(AppViewInteractor::class.java).value }

    RouteSwitch(interactor.coordinator) {
        when (it) {
            is Route.Home -> HomeScreen()
        }
    }
}