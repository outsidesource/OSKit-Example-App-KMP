package ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInject
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import ui.app.theme.AppTheme

@Composable
fun Screen(
    title: String,
    interactor: ScreenViewInteractor = rememberInjectForRoute(),
    paddingValues: PaddingValues = AppTheme.dimensions.screenPadding,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = interactor.collectAsState()

    Column {
        AppBar(
            modifier = Modifier.zIndex(1f),
            title = title,
            hasBackStack = state.hasBackStack,
            isDarkTheme = state.isDarkTheme,
            onThemeToggled = { interactor.onThemeToggled() },
            onBackPress = interactor::appBarBackButtonPressed
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = AppTheme.colors.screenBackground())
                .padding(paddingValues),
            content = content
        )
    }
}