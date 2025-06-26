package ui

import com.outsidesource.oskitkmp.router.IRoute
import com.outsidesource.oskitkmp.router.IWebRoute
import com.outsidesource.oskitkmp.router.Router

sealed class Route(
    override val webRoutePath: String?,
    override val webRouteTitle: String? = null
): IRoute, IWebRoute {
    data object Home: Route(webRoutePath = "/")
    data class ViewStateExample(val depth: Int): Route(webRoutePath = "/view-state/$depth")
    data object AppStateExample: Route(webRoutePath = "/app-state")
    data class DeviceHome(val deviceId: Int): Route(webRoutePath = "/devices/$deviceId")
    data object FileHandling: Route(webRoutePath = "/files")
    data object Markdown: Route(webRoutePath = "/markdown")
    data object Popups: Route(webRoutePath = "/popups")
    data object Resources: Route(webRoutePath = "/ui/resources")
    data object IOSServices: Route(webRoutePath = "/ios-services")
    data object Widgets: Route(webRoutePath = "/widgets")
    data object Capability : Route(webRoutePath = "/capability")
    data object ColorPicker : Route(webRoutePath = "/color-picker")
    data object WebDemo : Route(webRoutePath = "/web-demo")
    data object WindowInfo : Route(webRoutePath = "/window-info")
    data object LocalNotifications : Route(webRoutePath = "/local-notifications")

    companion object {
        val deepLinks = Router.buildDeepLinks {
            "/view-state/:depth" routesTo { args, _ -> ViewStateExample(args[0].toInt()) }
        }
    }
}