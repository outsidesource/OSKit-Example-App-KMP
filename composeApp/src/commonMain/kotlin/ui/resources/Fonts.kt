@file:OptIn(ExperimentalResourceApi::class)

package ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import oskit_example_app_kmp.composeapp.generated.resources.*

object Fonts {
    val Default = FontFamily.Default
    val SansSerifPro: FontFamily
        @Composable
        get() = FontFamily(
            Font(Res.font.SourceSansPro_Bold, weight = FontWeight.Bold),
            Font(Res.font.SourceSansPro_Light, weight = FontWeight.Light),
            Font(Res.font.SourceSansPro_Regular, weight = FontWeight.Normal),
            Font(Res.font.SourceSansPro_SemiBold, weight = FontWeight.SemiBold),
        )
}