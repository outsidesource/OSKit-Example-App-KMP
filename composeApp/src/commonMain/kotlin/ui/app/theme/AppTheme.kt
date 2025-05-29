package ui.app.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import com.outsidesource.oskitcompose.systemui.WindowSizeClass
import com.outsidesource.oskitcompose.systemui.widthSizeClass

val LocalAppColors = staticCompositionLocalOf<IAppColors> { LightAppColors }
val LocalAppTypography = staticCompositionLocalOf<IAppTypography> { AppTypography(LightAppColors, PhoneAppDimensions) }
val LocalAppDimensions = staticCompositionLocalOf<IAppDimensions> { PhoneAppDimensions }

object AppTheme {
    val colors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current

    val dimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalAppDimensions.current
}

@Composable
fun AppTheme(
    colorsOverride: IAppColors? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val sizeClass = LocalWindowInfo.current.widthSizeClass
    val colors = when {
        colorsOverride != null -> colorsOverride
        !isSystemInDarkTheme() -> LightAppColors
        else -> DarkAppColors
    }

    val dimensions = when {
        sizeClass < WindowSizeClass.Tablet -> PhoneAppDimensions
        sizeClass < WindowSizeClass.Desktop -> TabletAppDimensions
        else -> DesktopAppDimensions
    }
    val typography = remember(colors, dimensions) { AppTypography(colors, dimensions) }

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        LocalAppDimensions provides dimensions,
    ) {
        MaterialTheme(
            typography = Typography(
                defaultFontFamily = AppTheme.typography.defaultFontFamily,
                button = MaterialTheme.typography.button.copy(
                    fontFamily = AppTheme.typography.defaultFontFamily,
                    color = AppTheme.colors.onPrimary,
                )
            ),
            colors = MaterialTheme.colors.copy(
                primary = AppTheme.colors.primary,
                secondary = AppTheme.colors.primary,
                secondaryVariant = AppTheme.colors.switchOn,
                surface = AppTheme.colors.switchOff,
                onSurface = AppTheme.colors.fontColor,
            )
        ) {
            ProvideTextStyle(
                TextStyle(
                    fontFamily = AppTheme.typography.defaultFontFamily,
                    color = AppTheme.colors.fontColor,
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LightAppColors.screenBackground()),
                    content = content,
                )
            }
        }
    }
}