package ui.capability

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.modifier.borderBottom
import ui.app.theme.AppTheme
import ui.common.Screen

@Composable
fun CapabilityScreen(
    interactor: CapabilityScreenViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    Screen("Capability") {
        Row(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Tab(
                label = "Bluetooth",
                isActive = state.capabilityType == CapabilityType.Bluetooth,
                onClick = { interactor.capabilityTypeClicked(CapabilityType.Bluetooth) }
            )
            Tab(
                label = "Location",
                isActive = state.capabilityType == CapabilityType.Location,
                onClick = { interactor.capabilityTypeClicked(CapabilityType.Location) }
            )
        }

        Item("Status", state.status.toString())
        Spacer(modifier = Modifier.height(24.dp))

        Item("Has Permissions", interactor.capability.hasPermissions.toString())
        Button(
            enabled = interactor.capability.hasPermissions,
            onClick = interactor::requestPermissionsClicked,
        ) {
            Text("Request Permissions")
        }
        Spacer(modifier = Modifier.height(24.dp))

        Item("Has Enablable Service", interactor.capability.hasEnablableService.toString())
        Item("Supports Request Enable", interactor.capability.supportsRequestEnable.toString())
        Button(
            enabled = interactor.capability.supportsRequestEnable,
            onClick = interactor::requestEnableClicked,
        ) {
            Text("Request Enable")
        }
        Spacer(modifier = Modifier.height(24.dp))

        Item("Supports Open App Settings Screen", interactor.capability.supportsOpenAppSettingsScreen.toString())
        Button(
            enabled = interactor.capability.supportsOpenAppSettingsScreen,
            onClick = interactor::openAppSettingsScreenClicked,
        ) {
            Text("Open App Settings")
        }
        Spacer(modifier = Modifier.height(24.dp))

        Item("Supports Open Enable Settings Screen", interactor.capability.supportsOpenServiceSettingsScreen.toString())
        Button(
            enabled = interactor.capability.supportsOpenServiceSettingsScreen,
            onClick = interactor::openServiceSettingsScreenClicked,
        ) {
            Text("Open Enable Settings")
        }
    }
}

@Composable
private fun Item(key: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(text = "$key:", fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}

@Composable
private fun Tab(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .then(if (isActive) Modifier.borderBottom(AppTheme.colors.primary, 2.dp) else Modifier)
            .height(40.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}