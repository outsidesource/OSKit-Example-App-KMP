package com.outsidesource.oskitExample.composeUI.coordinator

import androidx.compose.animation.ExperimentalAnimationApi
import com.outsidesource.oskitExample.composeUI.ui.Route
import com.outsidesource.oskitkmp.coordinator.Coordinator
import com.outsidesource.oskitkmp.lib.Platform
import com.outsidesource.oskitkmp.lib.current
import com.outsidesource.oskitkmp.router.DefaultRouteTransition
import com.outsidesource.oskitkmp.router.ScaleRouteTransition

@OptIn(ExperimentalAnimationApi::class)
class AppCoordinator: Coordinator(
    initialRoute = Route.Home,
    defaultTransition = if (Platform.current.isMobile) ScaleRouteTransition else DefaultRouteTransition
) {
    fun coordinatorExample() = push(Route.CoordinatorExample)
    fun viewStateExample(depth: Int) = push(Route.ViewStateExample(depth))
    fun coordinatorHasBackStack() = hasBackStack()
    fun popToHome() = popTo(Route.Home)
    fun popBackStack() = pop()
}