package ui.iosServices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.resources.Strings
import ui.app.theme.AppTheme
import ui.common.Screen
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.resources.rememberKmpString
import com.outsidesource.oskitkmp.lib.Platform
import com.outsidesource.oskitkmp.lib.current

@Composable
fun IOSServicesScreen(
    interactor: IOSServicesScreenViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    Screen(rememberKmpString(Strings.iosServices)) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Section("Implemented with Swift") {
                Column{
                    Button(
                        content = { Text("Create Flow in Swift") },
                        onClick = interactor::createFlowInSwiftClicked
                    )
                    Text(state.createFlowInSwiftText)
                }
                Column {
                    Button(
                        content = { Text("Collect Flow in Swift") },
                        onClick = interactor::collectFlowInSwiftClicked
                    )
                    Text(state.collectFlowInSwiftText)
                }
                Column {
                    Button(
                        content = { Text("Suspend Function from Swift") },
                        onClick = interactor::suspendFunctionClicked
                    )
                    Text(state.suspendFunctionText)
                }
            }

            Section("UIKit") {
                UIKitViewExample(
                    modifier = Modifier
                        .size(200.dp)
                )
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionHeader(title)
        content()
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(text = text, style = TextStyle(fontWeight = FontWeight.Bold, color = AppTheme.colors.fontColor))
}