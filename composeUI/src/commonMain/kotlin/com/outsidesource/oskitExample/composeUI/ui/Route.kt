package com.outsidesource.oskitExample.composeUI.ui

import com.outsidesource.oskitkmp.router.IRoute

sealed class Route: IRoute {
    object Home: Route()
    data class ViewStateExample(val depth: Int): Route()
    object AppStateExample: Route()
    data class DeviceHome(val deviceId: Int): Route()
}