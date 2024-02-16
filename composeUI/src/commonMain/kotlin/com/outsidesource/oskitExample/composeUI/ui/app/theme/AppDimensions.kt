package com.outsidesource.oskitExample.composeUI.ui.app.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
interface IAppDimensions {
    val screenPadding: PaddingValues
}

@Immutable
private data object DefaultDimensions : IAppDimensions {
    override val screenPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp)
}

@Immutable
data object PhoneAppDimensions : IAppDimensions by DefaultDimensions

@Immutable
data object TabletAppDimensions : IAppDimensions by DefaultDimensions

@Immutable
data object DesktopAppDimensions : IAppDimensions by DefaultDimensions