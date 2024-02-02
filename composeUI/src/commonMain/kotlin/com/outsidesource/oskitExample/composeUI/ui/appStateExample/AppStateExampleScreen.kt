package com.outsidesource.oskitExample.composeUI.ui.appStateExample

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outsidesource.oskitExample.common.model.device.ConnectionStatus
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute

@Composable
fun AppStateExampleScreen(
    interactor: AppStateExampleViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    Screen("App State Example") {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (state.discoveredDevices.isEmpty() && !state.isDiscoveringDevices) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("No discovered devices. Please scan for devices")
                    Button(
                        content = { Text("Scan for devices") },
                        onClick = { interactor.discoverDevicesButtonClicked() }
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn {
                        items(state.discoveredDevices) {
                            Row(
                                modifier = Modifier
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple()
                                    ) {
                                        interactor.deviceSelected(it)
                                    }
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(it.name)
                                Text(text = it.model.toString(), fontSize = 12.sp, color = Color(0xFF777777))
                                Box(modifier = Modifier.clip(CircleShape).size(10.dp).background(when (it.connectionStatus) {
                                    ConnectionStatus.Connected -> Color.Green
                                    ConnectionStatus.Connecting -> Color.Yellow
                                    ConnectionStatus.Disconnected -> Color.Red
                                }))
                            }
                            if (it != state.discoveredDevices.last()) Divider(color = Color(0xFFF2F2F2))
                        }
                    }
                    if (state.isDiscoveringDevices) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).size(40.dp))
                    }
                }
            }
        }
    }
}