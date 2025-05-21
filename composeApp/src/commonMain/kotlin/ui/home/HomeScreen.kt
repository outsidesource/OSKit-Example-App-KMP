package ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.layout.FlexRowLayoutScope.weight
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.resources.rememberKmpString
import com.outsidesource.oskitcompose.scrollbars.KmpVerticalScrollbar
import com.outsidesource.oskitcompose.scrollbars.rememberKmpScrollbarAdapter
import com.outsidesource.oskitkmp.lib.Platform
import com.outsidesource.oskitkmp.lib.current
import ui.app.theme.AppTheme
import ui.common.Screen
import ui.resources.Strings

@Composable
fun HomeScreen(
    interactor: HomeViewInteractor = rememberInjectForRoute(),
) {
    Screen(
        title = "Home",
        paddingValues = PaddingValues(0.dp),
    ) {
        val scrollState = rememberScrollState()

        Box {
            KmpVerticalScrollbar(
                modifier = Modifier.align(Alignment.TopEnd),
                adapter = rememberKmpScrollbarAdapter(scrollState)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(AppTheme.dimensions.screenPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
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
            }
        }
    }
}