package ui.app

import androidx.compose.runtime.Composable
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInject
import com.outsidesource.oskitcompose.router.RouteSwitch
import com.outsidesource.oskitcompose.systemui.SystemBarColorEffect
import com.outsidesource.oskitcompose.systemui.SystemBarIconColor
import org.koin.core.parameter.parametersOf
import ui.Route
import ui.app.theme.AppTheme
import ui.app.theme.DarkAppColors
import ui.app.theme.LightAppColors
import ui.appStateExample.AppStateExampleScreen
import ui.capability.CapabilityScreen
import ui.colorPicker.ColorPickerScreen
import ui.device.DeviceHomeScreen
import ui.file.FileSystemScreen
import ui.home.HomeScreen
import ui.images.ResourcesScreen
import ui.iosServices.IOSServicesScreen
import ui.markdown.MarkdownScreen
import ui.popups.PopupsScreen
import ui.viewStateExample.ViewStateExampleScreen
import ui.htmlDemo.HtmlDemoScreen
import ui.widgets.WidgetsScreen

@Composable
fun App(
    deepLink: String? = null,
    interactor: AppViewInteractor = rememberInject<AppViewInteractor> { parametersOf(deepLink) }
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
                is Route.FileHandling -> FileSystemScreen()
                is Route.Resources -> ResourcesScreen()
                is Route.Markdown -> MarkdownScreen()
                is Route.Popups -> PopupsScreen()
                is Route.IOSServices -> IOSServicesScreen()
                is Route.Widgets -> WidgetsScreen()
                is Route.Capability -> CapabilityScreen()
                is Route.ColorPicker -> ColorPickerScreen()
                is Route.WebDemo -> HtmlDemoScreen(
                    transitionDirection = if (it == interactor.getActiveRoute()) {
                        RouteTransitionDirection.In
                    } else {
                        RouteTransitionDirection.Out
                    }
                )
            }
        }
    }
}

enum class RouteTransitionDirection {
    In,
    Out,
}