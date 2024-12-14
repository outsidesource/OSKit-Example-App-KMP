package ui

import com.outsidesource.oskitkmp.router.IRoute
import com.outsidesource.oskitkmp.router.IWebRoute

sealed class Route(
    override val path: String?,
    override val title: String? = null
): IRoute, IWebRoute {
    data object Home: Route(path = "/")
    data class ViewStateExample(val depth: Int): Route(path = "/view-state/$depth")
    data object AppStateExample: Route(path = "/app-state")
    data class DeviceHome(val deviceId: Int): Route(path = "/devices/$deviceId")
    data object FileHandling: Route(path = "/files")
    data object Markdown: Route(path = "/markdown")
    data object Popups: Route(path = "/popups")
    data object Resources: Route(path = "/resources")
    data object IOSServices: Route(path = "/ios-services")
    data object Widgets: Route(path = "/widgets")
}