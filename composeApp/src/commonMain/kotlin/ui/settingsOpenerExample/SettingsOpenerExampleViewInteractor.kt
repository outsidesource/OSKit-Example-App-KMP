package ui.settingsOpenerExample

import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.systemui.KmpSettingsScreenOpener
import com.outsidesource.oskitkmp.systemui.SettingsScreenType
import coordinator.AppCoordinator
import kotlinx.coroutines.launch

data class SettingsOpenerExampleViewState(
    val appSettingsOutcome: String? = null,
    val systemSettingsOutcome: String? = null,
    val btSettingsOutcome: String? = null,
    val locationSettingsOutcome: String? = null,
)

class SettingsOpenerExampleViewInteractor(
    private val coordinator: AppCoordinator,
    private val screenOpener: KmpSettingsScreenOpener
) : Interactor<SettingsOpenerExampleViewState>(
    initialState = SettingsOpenerExampleViewState()
) {

    override fun computed(state: SettingsOpenerExampleViewState): SettingsOpenerExampleViewState {
        return state
    }

    fun appSettingsClicked() {
        interactorScope.launch {
            val res = screenOpener.open(SettingsScreenType.App)
            update { state ->
                state.copy(
                    appSettingsOutcome = res.toString()
                )
            }
        }
    }

    fun systemSettingsClicked() {
        interactorScope.launch {
            val res = screenOpener.open(SettingsScreenType.SystemSettings)
            update { state ->
                state.copy(
                    systemSettingsOutcome = res.toString()
                )
            }
        }
    }

    fun bluetoothSettingsClicked() {
        interactorScope.launch {
            val res = screenOpener.open(SettingsScreenType.Bluetooth)
            update { state ->
                state.copy(
                    btSettingsOutcome = res.toString()
                )
            }
        }
    }

    fun locationSettingsClicked() {
        interactorScope.launch {
            val res = screenOpener.open(SettingsScreenType.Location)
            update { state ->
                state.copy(
                    locationSettingsOutcome = res.toString()
                )
            }
        }
    }
}