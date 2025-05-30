package ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.layout.VerticalGrid
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.resources.rememberKmpString
import com.outsidesource.oskitcompose.scrollbars.KmpVerticalScrollbar
import com.outsidesource.oskitcompose.scrollbars.rememberKmpScrollbarAdapter
import com.outsidesource.oskitcompose.systemui.WindowSizeClass
import com.outsidesource.oskitcompose.systemui.widthSizeClass
import com.outsidesource.oskitkmp.lib.Platform
import com.outsidesource.oskitkmp.lib.current
import ui.app.theme.AppTheme
import ui.common.Screen
import ui.resources.Strings

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    interactor: HomeViewInteractor = rememberInjectForRoute(),
) {
    Screen(
        title = "Home",
        paddingValues = PaddingValues(0.dp),
    ) {
        val scrollState = rememberScrollState()
        val windowSizeClass = LocalWindowInfo.current.widthSizeClass

        Box {
            KmpVerticalScrollbar(
                modifier = Modifier.align(Alignment.TopEnd),
                adapter = rememberKmpScrollbarAdapter(scrollState)
            )
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                VerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = AppTheme.dimensions.screenPadding.calculateStartPadding(LocalLayoutDirection.current))
                        .verticalScroll(scrollState),
                    columns = when {
                        windowSizeClass <= WindowSizeClass.Phone -> 1
                        windowSizeClass <= WindowSizeClass.Tablet -> 2
                        else -> 3
                    },
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                    contentPadding = PaddingValues(
                        top = AppTheme.dimensions.screenPadding.calculateTopPadding(),
                        bottom = AppTheme.dimensions.screenPadding.calculateTopPadding(),
                    ),
                ) {
                    Button(
                        content = { Text(rememberKmpString(Strings.appInteractorExample)) },
                        onClick = interactor::appStateExampleButtonClicked,
                    )
                    Button(
                        content = { Text(rememberKmpString(Strings.viewInteractorExample)) },
                        onClick = interactor::viewStateExampleButtonClicked,
                    )
                    Button(
                        content = { Text(rememberKmpString(Strings.fileSystem)) },
                        onClick = interactor::fileHandlingButtonClicked,
                    )
                    Button(
                        content = { Text(rememberKmpString(Strings.markdown)) },
                        onClick = interactor::markdownButtonClicked,
                    )
                    Button(
                        content = { Text(rememberKmpString(Strings.popups)) },
                        onClick = interactor::popupsButtonClicked,
                    )
                    Button(
                        content = { Text(rememberKmpString(Strings.resources)) },
                        onClick = interactor::resourcesButtonClicked,
                    )
                    Button(
                        content = { Text("Widgets") },
                        onClick = interactor::widgetsButtonClicked,
                    )
                    Button(
                        content = { Text("Capability") },
                        onClick = interactor::capabilityButtonClicked,
                    )
                    Button(
                        content = { Text("Color Picker") },
                        onClick = interactor::colorPickerButtonClicked,
                    )
                    Button(
                        content = { Text("Window Info") },
                        onClick = interactor::windowInfoButtonClicked,
                    )
                    Button(
                        content = { Text(rememberKmpString(Strings.iosServices)) },
                        onClick = interactor::iosServicesButtonClicked,
                        enabled = Platform.current == Platform.IOS,
                    )
                    Button(
                        content = { Text("HTML in WASM Demo") },
                        onClick = interactor::htmlDemoButtonClicked,
                        enabled = Platform.current == Platform.WebBrowser,
                    )
                    Button(
                        content = { Text("KV Store Demo") },
                        onClick = interactor::kvStoreButtonClicked,
                    )
                }
            }
        }
    }
}