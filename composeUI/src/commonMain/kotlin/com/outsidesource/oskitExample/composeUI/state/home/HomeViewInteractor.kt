package com.outsidesource.oskitExample.composeUI.state.home

import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitkmp.interactor.Interactor

class HomeViewInteractor(
    private val coordinator: AppCoordinator
): Interactor<Unit>(initialState = Unit) {

    fun coordinatorExampleButtonClicked() {
        coordinator.coordinatorExample()
    }

    fun viewStateExampleButtonClicked() {
        coordinator.viewStateExample(0)
    }
}