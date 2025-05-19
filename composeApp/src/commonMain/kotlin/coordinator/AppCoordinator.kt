package coordinator

import androidx.compose.animation.ExperimentalAnimationApi
import ui.Route
import com.outsidesource.oskitcompose.router.PushFromRightRouteTransition
import com.outsidesource.oskitcompose.router.PushFromTopRouteTransition
import com.outsidesource.oskitkmp.coordinator.Coordinator
import com.outsidesource.oskitkmp.lib.Platform
import com.outsidesource.oskitkmp.lib.current


@OptIn(ExperimentalAnimationApi::class)
class AppCoordinator(): Coordinator(
    initialRoute = Route.Home,
    defaultTransition = if (Platform.current.isMobile) PushFromRightRouteTransition else PushFromTopRouteTransition
) {

    fun handleDeepLink(deepLink: String?) {
        val route = Route.deepLinks.matchRoute(deepLink) ?: return
        push(route, ignoreTransitionLock = true)
    }

    fun coordinatorHasBackStack() = hasBackStack()
    fun popToHome() = transaction {
        pop { whileTrue { true } }
        replace(Route.Home)
    }
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
    fun capabilityClicked() = push(Route.Capability)
    fun colorPickerClicked() = push(Route.ColorPicker)
    fun settingsOpenerExampleClicked() = push(Route.SettingsOpenerExample)
    fun htmlDemoClicked() = push(Route.WebDemo)
}