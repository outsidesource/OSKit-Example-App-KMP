package com.outsidesource.oskitExample.composeUI.state.home

import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitkmp.interactor.Interactor

class HomeViewInteractor(
    private val coordinator: AppCoordinator
): Interactor<Unit>(initialState = Unit) {

    fun appStateExampleButtonClicked() {
        coordinator.appStateExample()
    }

    fun viewStateExampleButtonClicked() {
        coordinator.viewStateExample(0)
    }
}