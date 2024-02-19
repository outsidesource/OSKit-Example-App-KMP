package com.outsidesource.oskitExample.ui

import com.outsidesource.oskitkmp.router.IRoute

sealed class Route: IRoute {
    data object Home: Route()
    data class ViewStateExample(val depth: Int): Route()
    data object AppStateExample: Route()
    data class DeviceHome(val deviceId: Int): Route()
    data object FileHandling: Route()
    data object Markdown: Route()
    data object Popups: Route()
    data object Resources: Route()
    data object IOSServices: Route()
}