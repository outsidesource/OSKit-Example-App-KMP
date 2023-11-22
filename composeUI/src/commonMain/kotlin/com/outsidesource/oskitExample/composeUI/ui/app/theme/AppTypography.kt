package com.outsidesource.oskitExample.composeUI.ui.app.theme

import androidx.compose.ui.text.font.FontFamily
import com.outsidesource.oskitExample.composeUI.Fonts

interface IAppTypography {
    val default: FontFamily
}

data class AppTypography(val colors: IAppColors, val dimensions: IAppDimensions) : IAppTypography {
    override val default = Fonts.SansSerifPro
}