package com.outsidesource.oskitExample.composeUI.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.composeUI.ui.app.theme.AppTheme
import com.outsidesource.oskitcompose.modifier.outerShadow
import com.outsidesource.oskitcompose.systemui.KMPWindowInsets
import com.outsidesource.oskitcompose.systemui.topInsets

@Composable
fun AppBar(title: String, hasBackStack: Boolean, modifier: Modifier = Modifier, onBackPress: () -> Unit) {
    Column(
        modifier = modifier
            .outerShadow(blur = 8.dp, color = Color.Black.copy(alpha = .25f), offset = DpOffset(0.dp, 2.dp))
            .background(AppTheme.colors.primary)
            .windowInsetsPadding(KMPWindowInsets.topInsets)
    ) {
        TopAppBar(
            modifier = Modifier
                .clip(RectangleShape)
                .background(AppTheme.colors.primary),
            backgroundColor = AppTheme.colors.primary,
            contentColor = Color.White,
            title = { Text(title) },
            navigationIcon = if (!hasBackStack) null else {
                {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        )
    }
}