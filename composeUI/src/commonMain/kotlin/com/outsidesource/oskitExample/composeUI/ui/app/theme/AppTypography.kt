package com.outsidesource.oskitExample.composeUI.ui.app.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.outsidesource.oskitExample.composeUI.resources.Fonts

interface IAppTypography {
    val defaultFontFamily: FontFamily
    val sectionHeader: TextStyle
}

data class AppTypography(val colors: IAppColors, val dimensions: IAppDimensions) : IAppTypography {
    override val defaultFontFamily = Fonts.SansSerifPro
    override val sectionHeader = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
    )
}