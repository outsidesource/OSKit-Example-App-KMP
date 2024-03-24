package ui.appStateExample

import com.outsidesource.oskitExample.common.entity.device.Device
import com.outsidesource.oskitExample.common.interactor.device.DeviceInteractor
import coordinator.AppCoordinator
import com.outsidesource.oskitkmp.interactor.Interactor
import kotlinx.coroutines.launch

data class AppStateExampleViewState(
    val discoveredDevices: List<Device> = emptyList(),
    val isDiscoveringDevices: Boolean = false,
)

class AppStateExampleViewInteractor(
    private val coordinator: AppCoordinator,
    private val deviceInteractor: DeviceInteractor,
): Interactor<AppStateExampleViewState>(
    initialState = AppStateExampleViewState(),
    dependencies = listOf(deviceInteractor),
) {

    override fun computed(state: AppStateExampleViewState): AppStateExampleViewState {
        return state.copy(
            discoveredDevices = deviceInteractor.state.devices.values.toList(),
        )
    }

    fun discoverDevicesButtonClicked() = interactorScope.launch {
        update { state -> state.copy(isDiscoveringDevices = true) }
        deviceInteractor.discoverDevices()
        update { state -> state.copy(isDiscoveringDevices = false) }
    }

    fun deviceSelected(device: Device) {
        coordinator.deviceClicked(device.id)
    }
}