package ui.app

import interactor.app.AppInteractor
import com.outsidesource.oskitkmp.interactor.Interactor
import coordinator.AppCoordinator

data class AppViewState(
    val isDarkTheme: Boolean = false,
)

class AppViewInteractor(
    deepLink: String?,
    private val appInteractor: AppInteractor,
    val coordinator: AppCoordinator,
) : Interactor<AppViewState>(
    initialState = AppViewState(),
    dependencies = listOf(appInteractor)
) {

    init {
        coordinator.handleDeepLink(deepLink)
    }

    override fun computed(state: AppViewState): AppViewState {
        return state.copy(isDarkTheme = appInteractor.state.isDarkTheme)
    }
}