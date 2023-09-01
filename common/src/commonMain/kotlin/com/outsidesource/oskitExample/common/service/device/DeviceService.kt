package com.outsidesource.oskitExample.common.service.device

import com.outsidesource.oskitExample.common.model.device.ConnectionStatus
import com.outsidesource.oskitExample.common.model.device.Device
import com.outsidesource.oskitExample.common.model.device.DeviceMode
import com.outsidesource.oskitExample.common.model.device.DeviceModel
import com.outsidesource.oskitkmp.outcome.Outcome
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class DeviceService {
    // These variables are just here to mock persistent device state
    private val volumes: MutableMap<Int, Float> = mutableMapOf()
    private val modes: MutableMap<Int, DeviceMode> = mutableMapOf()
    private val connectionStatuses: MutableMap<Int, ConnectionStatus> = mutableMapOf()

    suspend fun discoverDevices() = flow {
        for (i in 0..10) {
            randomDelay(100, 1000)
            emit(Device(i, "Device $i", DeviceModel.values()[i % DeviceModel.values().size]))
        }
    }

    suspend fun setVolume(id: Int, volume: Float): Outcome<Unit, Any> {
        delay(100)
        volumes[id] = volume
        return Outcome.Ok(Unit)
    }

    suspend fun setMode(id: Int, mode: DeviceMode): Outcome<Unit, Any> {
        delay(100)
        modes[id] = mode
        return Outcome.Ok(Unit)
    }

    suspend fun getVolume(id: Int): Outcome<Float, Any> {
        randomDelay(50, 150)
        return Outcome.Ok(volumes[id] ?: 0f)
    }

    suspend fun getMode(id: Int): Outcome<DeviceMode, Any> {
        randomDelay(50, 150)
        return Outcome.Ok(modes[id] ?: DeviceMode.Listen)
    }

    suspend fun connect(id: Int): Outcome<Unit, Any> {
        delay(750)
        connectionStatuses[id] = ConnectionStatus.Connected
        return Outcome.Ok(Unit)
    }

    fun disconnect(id: Int): Outcome<Unit, Any> {
        connectionStatuses[id] = ConnectionStatus.Disconnected
        return Outcome.Ok(Unit)
    }

    private suspend fun randomDelay(min: Int, max: Int) = delay(((Random.nextInt() * (max - min)) + min).toLong())
}