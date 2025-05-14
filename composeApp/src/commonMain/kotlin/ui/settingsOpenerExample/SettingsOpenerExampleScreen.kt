package ui.settingsOpenerExample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.common.Screen
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import ui.viewStateExample.ViewStateExampleViewInteractor

@Composable
fun SettingsOpenerExampleScreen(
    interactor: SettingsOpenerExampleViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    Screen("Settings Opener Example") {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Button(
                content = { Text("Open App Settings") },
                onClick = interactor::appSettingsClicked
            )

            Button(
                content = { Text("Open System Settings") },
                onClick = interactor::systemSettingsClicked
            )

            Button(
                content = { Text("Open Bluetooth Settings") },
                onClick = interactor::bluetoothSettingsClicked
            )

            Button(
                content = { Text("Open Location Settings") },
                onClick = interactor::locationSettingsClicked
            )

            Button(
                content = { Text("Pop To Home") },
                onClick = interactor::popToHome
            )
        }
    }
}