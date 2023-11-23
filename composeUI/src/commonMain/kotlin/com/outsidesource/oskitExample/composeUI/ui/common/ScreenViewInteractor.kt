package com.outsidesource.oskitExample.composeUI.ui.common

import com.outsidesource.oskitExample.common.interactor.app.AppInteractor
import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.interactor.Interactor
import org.koin.core.component.inject

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