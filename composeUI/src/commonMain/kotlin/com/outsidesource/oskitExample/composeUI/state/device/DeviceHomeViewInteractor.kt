package com.outsidesource.oskitExample.composeUI.state.device

import com.outsidesource.oskitExample.common.lib.Debouncer
import com.outsidesource.oskitExample.common.model.device.ConnectionStatus
import com.outsidesource.oskitExample.common.model.device.Device
import com.outsidesource.oskitExample.common.model.device.DeviceMode
import com.outsidesource.oskitExample.common.state.device.DeviceInteractor
import com.outsidesource.oskitkmp.interactor.Interactor
import kotlinx.coroutines.launch

data class DeviceHomeViewState(
    val device: Device? = null,
)

class DeviceHomeViewInteractor(
    private val deviceId: Int,
    private val deviceInteractor: DeviceInteractor,
): Interactor<DeviceHomeViewState>(
    initialState = DeviceHomeViewState(),
    dependencies = listOf(deviceInteractor)
) {
    private val debouncer = Debouncer(200, maxWaitMillis = 200)

    override fun computed(state: DeviceHomeViewState): DeviceHomeViewState {
        return state.copy(
            device = deviceInteractor.state.devices[deviceId],
        )
    }

    fun viewMounted() {
        if (state.device?.connectionStatus == ConnectionStatus.Connected) return
        connect()
    }

    fun connect() = interactorScope.launch {
        deviceInteractor.connect(deviceId)
    }

    fun disconnect() = interactorScope.launch {
        deviceInteractor.disconnect(deviceId)
    }

    fun setVolume(volume: Float) = interactorScope.launch {
        debouncer.emit {
            deviceInteractor.setVolume(deviceId, volume)
        }
    }

    fun setMode(mode: DeviceMode) = interactorScope.launch {
        deviceInteractor.setMode(deviceId, mode)
    }
}