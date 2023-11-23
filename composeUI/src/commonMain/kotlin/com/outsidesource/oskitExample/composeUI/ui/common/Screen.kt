package com.outsidesource.oskitExample.composeUI.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.outsidesource.oskitExample.composeUI.ui.app.theme.AppTheme
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInject

@Composable
fun Screen(
    title: String,
    interactor: ScreenViewInteractor = rememberInject(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = interactor.collectAsState()

    Column {
        AppBar(
            modifier = Modifier.zIndex(1f),
            title = title,
            hasBackStack = interactor.hasBackStack(),
            isDarkTheme = state.isDarkTheme,
            onThemeToggled = { interactor.onThemeToggled() },
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