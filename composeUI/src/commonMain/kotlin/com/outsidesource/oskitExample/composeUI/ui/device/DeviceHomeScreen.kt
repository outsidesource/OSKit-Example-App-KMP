package com.outsidesource.oskitExample.composeUI.ui.device

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.common.model.device.ConnectionStatus
import com.outsidesource.oskitExample.common.model.device.Device
import com.outsidesource.oskitExample.common.model.device.DeviceMode
import com.outsidesource.oskitExample.composeUI.state.device.DeviceHomeViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import com.outsidesource.oskitkmp.interactor.collectAsState
import com.outsidesource.oskitkmp.lib.ValRef
import com.outsidesource.oskitkmp.lib.rememberValRef
import com.outsidesource.oskitkmp.router.RouteDestroyedEffect
import com.outsidesource.oskitkmp.router.rememberForRoute
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

@Composable
fun DeviceHomeScreen(
    deviceId: Int,
    interactor: DeviceHomeViewInteractor = rememberForRoute {
        inject<DeviceHomeViewInteractor>(DeviceHomeViewInteractor::class.java, parameters = { parametersOf(deviceId) }).value
    }
) {
    val state by interactor.collectAsState()

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
    val state by interactorRef.value.collectAsState()
    val interactor = interactorRef.value
    val device = state.device ?: return

    var localVolume by remember(device.volume) { mutableStateOf(device.volume) }

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
            DeviceMode.values().forEach {
                ModeButton(isSelected = device.mode == it, mode = it, onClick = { interactor.setMode(it) })
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Volume")
            Slider(
                value = localVolume * 100f,
                valueRange = 0f..100f,
                onValueChange = {
                    localVolume = it / 100f
                    interactor.setVolume(it / 100f)
                },
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
                color = if (isSelected) Color.Black else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(mode.toString())
    }
}