package ui.common

import interactor.app.AppInteractor
import coordinator.AppCoordinator
import com.outsidesource.oskitkmp.interactor.Interactor

data class ScreenViewState(
    val isDarkTheme: Boolean = false,
)

class ScreenViewInteractor(
    private val appInteractor: AppInteractor,
    private val coordinator: AppCoordinator,
) : Interactor<ScreenViewState>(
    initialState = ScreenViewState(),
    dependencies = listOf(appInteractor)
) {

    override fun computed(state: ScreenViewState): ScreenViewState {
        return state.copy(isDarkTheme = appInteractor.state.isDarkTheme)
    }

    fun onThemeToggled() { appInteractor.onThemeToggled() }
    fun hasBackStack() = coordinator.coordinatorHasBackStack()
    fun appBarBackButtonPressed() = coordinator.popBackStack()
}