package com.outsidesource.oskitExample.coordinator

import androidx.compose.animation.ExperimentalAnimationApi
import com.outsidesource.oskitExample.ui.Route
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
    fun coordinatorHasBackStack() = hasBackStack()
    fun popToHome() = popTo(Route.Home)
    fun popBackStack() = pop()

    fun appStateExampleClicked() = push(Route.AppStateExample)
    fun viewStateExampleClicked(depth: Int) = push(Route.ViewStateExample(depth))
    fun deviceClicked(deviceId: Int) = push(Route.DeviceHome(deviceId))
    fun fileHandlingClicked() = push(Route.FileHandling)
    fun markdownClicked() = push(Route.Markdown)
    fun popupsClicked() = push(Route.Popups)
    fun resourcesClicked() = push(Route.Resources)
    fun iosServicesClicked() = push(Route.IOSServices)
    fun widgetsClicked() = push(Route.Widgets)
}