package com.outsidesource.oskitExample.composeUI.ui.app.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

interface IAppDimensions {
    val screenPadding: PaddingValues
}

private data object DefaultDimensions : IAppDimensions {
    override val screenPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp)
}

data object PhoneAppDimensions : IAppDimensions by DefaultDimensions {
}

data object TabletAppDimensions : IAppDimensions by DefaultDimensions {
}

data object DesktopAppDimensions : IAppDimensions by DefaultDimensions {
}