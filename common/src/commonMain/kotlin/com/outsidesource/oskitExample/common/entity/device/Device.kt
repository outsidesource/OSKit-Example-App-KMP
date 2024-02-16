package com.outsidesource.oskitExample.common.entity.device

data class Device(
    val id: Int = 0,
    val name: String = "",
    val model: DeviceModel = DeviceModel.BlueFire,
    val connectionStatus: ConnectionStatus = ConnectionStatus.Disconnected,
    val volume: Float = 0f,
    val mode: DeviceMode = DeviceMode.Listen,
)

enum class DeviceMode {
    Listen,
    Talk,
    Ludicrous,
}

enum class DeviceModel {
    RedWagon,
    BlueFire,
    GreenTree,
    OrangeOrange;

    override fun toString(): String = when(this) {
        RedWagon -> "Red Wagon"
        BlueFire -> "Blue Fire"
        GreenTree -> "Green Tree"
        OrangeOrange -> "Orange Orange"
    }
}

enum class ConnectionStatus {
    Disconnected,
    Connecting,
    Connected,
}