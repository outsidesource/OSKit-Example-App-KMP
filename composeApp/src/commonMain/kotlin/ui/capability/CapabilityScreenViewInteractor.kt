package ui.capability

import com.outsidesource.oskitkmp.capability.CapabilityStatus
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.outcome.unwrapOrReturn
import kotlinx.coroutines.launch

data class CapabilityScreenViewState(
    val capabilityType: CapabilityType = CapabilityType.Bluetooth,
    val status: CapabilityStatus = CapabilityStatus.Unknown,
)

class CapabilityScreenViewInteractor(
    private val capabilityService: KmpCapabilities,
) : Interactor<CapabilityScreenViewState>(
    initialState = CapabilityScreenViewState(status = capabilityService.bluetooth.status)
) {

    val capability
        get() = when (state.capabilityType) {
            CapabilityType.Bluetooth -> capabilityService.bluetooth
            CapabilityType.Location -> capabilityService.location
        }

    fun capabilityTypeClicked(type: CapabilityType) {
        val capability = when (type) {
            CapabilityType.Bluetooth -> capabilityService.bluetooth
            CapabilityType.Location -> capabilityService.location
        }

        update { state ->
            state.copy(
                capabilityType = type,
                status = capability.status
            )
        }
    }

    fun requestPermissionsClicked() {
        interactorScope.launch {
            val status = capability.requestPermissions().unwrapOrReturn { return@launch }
            update { state -> state.copy(status = status) }
        }
    }

    fun requestEnableClicked() {
        interactorScope.launch {
            val status = capability.requestEnable().unwrapOrReturn { return@launch }
            update { state -> state.copy(status = status) }
        }
    }

    fun openAppSettingsScreenClicked() {
        interactorScope.launch {
            capability.openAppSettingsScreen()
        }
    }

    fun openServiceSettingsScreenClicked() {
        interactorScope.launch {
            capability.openServiceSettingsScreen()
        }
    }
}

enum class CapabilityType {
    Bluetooth,
    Location,
}