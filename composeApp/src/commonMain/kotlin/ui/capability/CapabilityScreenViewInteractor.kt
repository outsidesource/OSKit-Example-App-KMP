package ui.capability

import com.outsidesource.oskitkmp.capability.CapabilityStatus
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.outcome.unwrapOrReturn
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

data class CapabilityScreenViewState(
    val capabilityType: CapabilityType = CapabilityType.Bluetooth,
    val status: CapabilityStatus = CapabilityStatus.Unknown,
)

class CapabilityScreenViewInteractor(
    private val capabilityService: KmpCapabilities,
) : Interactor<CapabilityScreenViewState>(
    initialState = CapabilityScreenViewState(status = CapabilityStatus.Unknown)
) {

    var capabilityStatusJob: Job? = null

    val capability
        get() = when (state.capabilityType) {
            CapabilityType.Bluetooth -> capabilityService.bluetooth
            CapabilityType.Location -> capabilityService.location
            CapabilityType.Notification -> capabilityService.location
        }

    init {
        listenToStatus()
    }

    fun capabilityTypeClicked(type: CapabilityType) {
        update { state -> state.copy(capabilityType = type) }
        listenToStatus()
    }

    fun listenToStatus() {
        capabilityStatusJob?.cancel()
        capabilityStatusJob = interactorScope.launch {
            capability.status.collect {
                update { state -> state.copy(status = it) }
            }
        }
    }

    fun requestPermissionsClicked() {
        interactorScope.launch {
            capability.requestPermissions().unwrapOrReturn { return@launch }
        }
    }

    fun requestEnableClicked() {
        interactorScope.launch {
            capability.requestEnable().unwrapOrReturn { return@launch }
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
    Notification,
}