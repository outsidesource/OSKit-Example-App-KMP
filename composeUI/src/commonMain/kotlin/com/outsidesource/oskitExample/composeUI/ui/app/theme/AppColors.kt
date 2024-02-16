package com.outsidesource.oskitExample.composeUI.ui.app.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

@Immutable
interface IAppColors {
    val screenBackground: () -> Brush
    val primary: Color
    val switchOff: Color
    val switchOn: Color
    val fontColor: Color
    val popupFontColor: Color
    val onPrimary: Color
}

@Immutable
data object LightAppColors: IAppColors {
    override val screenBackground = { SolidColor(Color.White) }
    override val primary = Color(0xFF1d4991)
    override val switchOff = Color.White
    override val switchOn = Color(0xFFDDDDDDD)
    override val fontColor = Color(0xFF000000)
    override val onPrimary = Color(0xFFFFFFFF)
    override val popupFontColor = Color(0xFF000000)
}

@Immutable
data object DarkAppColors: IAppColors by LightAppColors {
    override val screenBackground = { SolidColor(Color(0xFF2222222)) }
    override val primary = Color(0xFF1d4991)
    override val fontColor = Color(0xFFE6E6E6)
}