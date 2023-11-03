package com.outsidesource.oskitExample.composeUI.coordinator

import androidx.compose.animation.ExperimentalAnimationApi
import com.outsidesource.oskitExample.composeUI.ui.Route
import com.outsidesource.oskitcompose.router.PushFromRightRouteTransition
import com.outsidesource.oskitcompose.router.PushFromTopRouteTransition
import com.outsidesource.oskitkmp.coordinator.Coordinator
import com.outsidesource.oskitkmp.lib.Platform
import com.outsidesource.oskitkmp.lib.current


@OptIn(ExperimentalAnimationApi::class)
class AppCoordinator: Coordinator(
    initialRoute = Route.Home,
    defaultTransition = if (Platform.current.isMobile) PushFromRightRouteTransition else PushFromTopRouteTransition
) {
    fun appStateExample() = push(Route.AppStateExample)
    fun viewStateExample(depth: Int) = push(Route.ViewStateExample(depth))
    fun coordinatorHasBackStack() = hasBackStack()
    fun deviceHome(deviceId: Int) = push(Route.DeviceHome(deviceId))
    fun fileHandling() = push(Route.FileHandling)
    fun markdown() = push(Route.Markdown)
    fun popups() = push(Route.Popups)
    fun images() = push(Route.Images)
    fun popToHome() = popTo(Route.Home)
    fun popBackStack() = pop()
}