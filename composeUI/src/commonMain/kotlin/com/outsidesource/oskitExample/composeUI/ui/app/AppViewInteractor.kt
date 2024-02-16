package com.outsidesource.oskitExample.composeUI.ui.app

import com.outsidesource.oskitExample.common.interactor.app.AppInteractor
import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.interactor.Interactor
import org.koin.core.component.inject

data class AppViewState(
    val isDarkTheme: Boolean = false,
)

class AppViewInteractor(
    private val appInteractor: AppInteractor,
) : Interactor<AppViewState>(
    initialState = AppViewState(),
    dependencies = listOf(appInteractor)
) {
    val coordinator by koinInjector.inject<AppCoordinator>()

    override fun computed(state: AppViewState): AppViewState {
        return state.copy(isDarkTheme = appInteractor.state.isDarkTheme)
    }
}