package com.outsidesource.oskitExample.composeUI.ui.app.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

interface IAppColors {
    val screenBackground: () -> Brush
    val primary: Color
}

data object LightAppColors: IAppColors {
    override val screenBackground = { SolidColor(Color.White) }
    override val primary = Color(0xFF1d4991)
}

data object DarkAppColors: IAppColors by LightAppColors {
    override val screenBackground = { SolidColor(Color.Black) }
    override val primary = Color(0xFF1d4991)
}