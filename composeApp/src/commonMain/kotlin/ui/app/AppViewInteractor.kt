package ui.app

import com.outsidesource.oskitkmp.coordinator.Coordinator
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

    private val coordinatorObserver = Coordinator.createObserver(coordinator)

    init {
        coordinator.handleDeepLink(deepLink)
    }

    override fun computed(state: AppViewState): AppViewState {
        return state.copy(isDarkTheme = appInteractor.state.isDarkTheme)
    }

    fun getActiveRoute() = coordinatorObserver.routeFlow.value.route
}