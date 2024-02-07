package com.outsidesource.oskitExample.common.interactor.device

import com.outsidesource.oskitExample.common.entity.device.ConnectionStatus
import com.outsidesource.oskitExample.common.entity.device.Device
import com.outsidesource.oskitExample.common.entity.device.DeviceMode
import com.outsidesource.oskitExample.common.service.device.DeviceService
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.outcome.Outcome

data class DevicesState(
    val devices: Map<Int, Device> = emptyMap(),
)

class DeviceInteractor(
    private val deviceService: DeviceService,
): Interactor<DevicesState>(initialState = DevicesState()) {

    suspend fun discoverDevices() {
        deviceService.discoverDevices().collect { device ->
            update { state -> state.copy(devices = state.devices.toMutableMap().apply { put(device.id, device) }) }
        }
    }

    suspend fun setVolume(id: Int, volume: Float): Outcome<Unit, Any> {
        val outcome = deviceService.setVolume(id, volume)
        if (outcome is Outcome.Ok) updateDevice(id) { settings -> settings.copy(volume = volume) }
        return outcome
    }

    suspend fun setMode(id: Int, mode: DeviceMode): Outcome<Unit, Any> {
        val outcome = deviceService.setMode(id, mode)
        if (outcome is Outcome.Ok) updateDevice(id) { settings -> settings.copy(mode = mode) }
        return outcome
    }

    suspend fun connect(id: Int) {
        updateDevice(id) { deviceState -> deviceState.copy(connectionStatus = ConnectionStatus.Connecting)}

        val outcome = deviceService.connect(id)
        getVolume(id)
        getMode(id)

        if (outcome is Outcome.Ok) {
            updateDevice(id) { deviceState -> deviceState.copy(connectionStatus = ConnectionStatus.Connected)}
        } else {
            updateDevice(id) { deviceState -> deviceState.copy(connectionStatus = ConnectionStatus.Disconnected)}
        }
    }

    private suspend fun getVolume(id: Int) {
        val outcome = deviceService.getVolume(id)
        if (outcome is Outcome.Ok) updateDevice(id) { settings -> settings.copy(volume = outcome.value) }
    }

    private suspend fun getMode(id: Int) {
        val outcome = deviceService.getMode(id)
        if (outcome is Outcome.Ok) updateDevice(id) { settings -> settings.copy(mode = outcome.value) }
    }

    fun disconnect(id: Int) {
        val outcome = deviceService.disconnect(id)

        if (outcome is Outcome.Ok) {
            updateDevice(id) { deviceState -> deviceState.copy(connectionStatus = ConnectionStatus.Disconnected)}
        }
    }

    private fun updateDevice(id: Int, block: (Device) -> Device) {
        update { state -> state.copy(devices = state.devices.toMutableMap().apply {
            put(id, block(this[id] ?: Device()))
        }) }
    }
}