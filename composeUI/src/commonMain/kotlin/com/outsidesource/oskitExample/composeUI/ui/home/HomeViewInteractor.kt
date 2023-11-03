package com.outsidesource.oskitExample.composeUI.ui.home

import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.router.IRouteLifecycleListener

class HomeViewInteractor(
    private val coordinator: AppCoordinator
): Interactor<Unit>(initialState = Unit) {

    fun appStateExampleButtonClicked() {
        coordinator.appStateExample()
    }

    fun viewStateExampleButtonClicked() {
        coordinator.viewStateExample(0)
    }

    fun fileHandlingButtonClicked() {
        coordinator.fileHandling()
    }

    fun markdownButtonClicked() {
        coordinator.markdown()
    }

    fun popupsButtonClicked() {
        coordinator.popups()
    }

    fun imagesButtonClicked() {
        coordinator.images()
    }
}