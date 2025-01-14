package ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import ui.app.theme.AppTheme
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInject
import com.outsidesource.oskitcompose.systemui.KmpWindowInsets
import com.outsidesource.oskitcompose.systemui.horizontal

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
                .windowInsetsPadding(KmpWindowInsets.horizontal)
                .padding(AppTheme.dimensions.screenPadding),
            content = content
        )
    }
}