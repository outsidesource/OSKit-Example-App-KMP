package ui.app.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import resources.Fonts

@Immutable
interface IAppTypography {
    val defaultFontFamily: FontFamily
    val sectionHeader: TextStyle
    val label: TextStyle
}

@Immutable
data class AppTypography(val colors: IAppColors, val dimensions: IAppDimensions) : IAppTypography {
    override val defaultFontFamily = Fonts.SansSerifPro
    override val sectionHeader = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = colors.fontColor,
    )
    override val label = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = colors.fontColor,
    )
}