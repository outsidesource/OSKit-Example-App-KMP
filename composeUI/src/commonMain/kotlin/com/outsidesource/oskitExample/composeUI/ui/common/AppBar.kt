package com.outsidesource.oskitExample.composeUI.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.composeUI.resources.Images
import com.outsidesource.oskitExample.composeUI.ui.app.theme.AppTheme
import com.outsidesource.oskitcompose.modifier.outerShadow
import com.outsidesource.oskitcompose.resources.rememberKmpImagePainter
import com.outsidesource.oskitcompose.systemui.*

@Composable
fun AppBar(
    title: String,
    hasBackStack: Boolean,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeToggled: (Boolean) -> Unit,
    onBackPress: () -> Unit,
) {

    Column(
        modifier = modifier
            .outerShadow(blur = 8.dp, color = Color.Black.copy(alpha = .25f), offset = DpOffset(0.dp, 2.dp))
            .background(AppTheme.colors.primary)
            .windowInsetsPadding(KMPWindowInsets.topInsets)
            .windowInsetsPadding(KMPWindowInsets.horizontalInsets)
    ) {
        TopAppBar(
            modifier = Modifier
                .clip(RectangleShape)
                .background(AppTheme.colors.primary),
            backgroundColor = AppTheme.colors.primary,
            contentColor = Color.White,
            title = { Text(text = title, color = AppTheme.colors.onPrimary) },
            navigationIcon = if (!hasBackStack) null else {
                {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            },
            actions = {
                Switch(checked = isDarkTheme, onCheckedChange = onThemeToggled)
                Image(
                    modifier = Modifier.padding(end = 8.dp).size(24.dp),
                    painter = rememberKmpImagePainter(Images.DarkMode),
                    contentDescription = "Dark Mode",
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }
        )
    }
}