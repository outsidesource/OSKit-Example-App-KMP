package ui.settingsOpenerExample

import coordinator.AppCoordinator
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.systemui.KmpSettingsScreen
import com.outsidesource.oskitkmp.systemui.SettingsScreenType
import kotlinx.coroutines.launch

data class SettingsOpenerExampleViewState(
    val text: Boolean = true
)

class SettingsOpenerExampleViewInteractor(
    private val coordinator: AppCoordinator,
    private val screenOpener: KmpSettingsScreen
): Interactor<SettingsOpenerExampleViewState>(
    initialState = SettingsOpenerExampleViewState()
) {

    override fun computed(state: SettingsOpenerExampleViewState): SettingsOpenerExampleViewState {
        return state
    }

    fun popToHome() = coordinator.popToHome()

    fun appSettingsClicked() {
        interactorScope.launch {
            screenOpener.open(SettingsScreenType.App)
        }
    }

    fun systemSettingsClicked() {
        interactorScope.launch {
            screenOpener.open(SettingsScreenType.SystemSettings)
        }
    }

    fun bluetoothSettingsClicked() {
        interactorScope.launch {
            screenOpener.open(SettingsScreenType.Bluetooth)
        }
    }

    fun locationSettingsClicked() {
        interactorScope.launch {
            screenOpener.open(SettingsScreenType.Location)
        }
    }
}