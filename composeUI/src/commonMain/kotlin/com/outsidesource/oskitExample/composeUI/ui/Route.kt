package com.outsidesource.oskitExample.composeUI.ui

import com.outsidesource.oskitkmp.router.IRoute

sealed class Route: IRoute {
    object Home: Route()
}