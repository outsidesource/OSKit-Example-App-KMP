package com.outsidesource.oskitExample.ui.device

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.common.entity.device.ConnectionStatus
import com.outsidesource.oskitExample.common.entity.device.DeviceMode
import com.outsidesource.oskitExample.ui.app.theme.AppTheme
import com.outsidesource.oskitExample.ui.common.CustomSlider
import com.outsidesource.oskitExample.ui.common.Screen
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.ValRef
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.lib.rememberValRef
import org.koin.core.parameter.parametersOf

@Composable
fun DeviceHomeScreen(
    deviceId: Int,
    interactor: DeviceHomeViewInteractor = rememberInjectForRoute { parametersOf(deviceId) }
) {
    val state = interactor.collectAsState()
    val device = state.device ?: return

    LaunchedEffect(Unit) {
        interactor.viewMounted()
    }

    Screen(device.name) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (device.connectionStatus) {
                ConnectionStatus.Connecting -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(40.dp))
                        Text("Connecting")
                    }
                }

                ConnectionStatus.Disconnected -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text("Disconnected")
                        Button(
                            content = { Text("Connect") },
                            onClick = { interactor.connect() }
                        )
                    }
                }

                ConnectionStatus.Connected -> {
                    DeviceView(rememberValRef(interactor))
                }
            }
        }
    }
}

@Composable
private fun DeviceView(
    interactorRef: ValRef<DeviceHomeViewInteractor>
) {
    val state = interactorRef.value.collectAsState()
    val interactor = interactorRef.value
    val device = state.device ?: return

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text("Mode")
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DeviceMode.entries.forEach {
                ModeButton(isSelected = device.mode == it, mode = it, onClick = { interactor.setMode(it) })
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Volume")
            CustomSlider(
                value = device.volume * 100f,
                valueRange = 0f..100f,
                onValueChange = { interactor.setVolume(it / 100f) },
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            content = { Text("Disconnect") },
            onClick = { interactor.disconnect() }
        )
    }
}

@Composable
fun ModeButton(isSelected: Boolean, mode: DeviceMode, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) AppTheme.colors.primary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = mode.toString(),
            color = Color.Black,
        )
    }
}