package ui.app.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.outsidesource.oskitcompose.form.KmpSliderStyle

@Immutable
interface IAppColors {
    val screenBackgroundColor: Color
    val screenBackground: () -> Brush
    val primary: Color
    val switchOff: Color
    val switchOn: Color
    val fontColor: Color
    val popupFontColor: Color
    val onPrimary: Color
    val menu: Color
    val menuBorder: Color
    val sliderStyles: KmpSliderStyle
}

@Immutable
data object LightAppColors: IAppColors {
    override val screenBackgroundColor = Color.White
    override val screenBackground = { SolidColor(Color.White) }
    override val primary = Color(0xFF1d4991)
    override val switchOff = Color.White
    override val switchOn = Color(0xFFDDDDDDD)
    override val fontColor = Color(0xFF000000)
    override val onPrimary = Color(0xFFFFFFFF)
    override val popupFontColor = Color(0xFF000000)
    override val menu: Color = Color(0xFFF2F2F2)
    override val menuBorder: Color = Color(0xFFE2E2E2)
    override val sliderStyles: KmpSliderStyle = KmpSliderStyle()
}

@Immutable
data object DarkAppColors: IAppColors by LightAppColors {
    override val screenBackgroundColor = Color(0xFF222222)
    override val screenBackground = { SolidColor(Color(0xFF222222)) }
    override val primary = Color(0xFF1d4991)
    override val fontColor = Color(0xFFE6E6E6)
    override val menu: Color = Color(0xFF444444)
    override val menuBorder: Color = Color(0xFF555555)
    override val sliderStyles: KmpSliderStyle = KmpSliderStyle().let {
        it.copy(
            trackFill = SolidColor(primary),
            trackBackground = SolidColor(Color(0xFF666666)),
            thumbBackground = SolidColor(Color(0xFFCCCCCC)),
            labelTextStyle = it.labelTextStyle.copy(color = fontColor),

        )
    }
}