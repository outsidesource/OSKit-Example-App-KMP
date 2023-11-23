package com.outsidesource.oskitExample.composeUI.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.outsidesource.oskitExample.composeUI.ui.app.AppViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.app.theme.AppTheme
import com.outsidesource.oskitcompose.lib.rememberInject
import com.outsidesource.oskitcompose.systemui.KMPWindowInsets
import com.outsidesource.oskitcompose.systemui.bottomInsets

@Composable
fun Screen(
    title: String,
    interactor: AppViewInteractor = rememberInject(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        AppBar(
            modifier = Modifier.zIndex(1f),
            title = title,
            hasBackStack = interactor.hasBackStack(),
            onBackPress = interactor::appBarBackButtonPressed
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = AppTheme.colors.screenBackground())
                .padding(AppTheme.dimensions.screenPadding),
            content = content
        )
    }
}