package ui.home

import coordinator.AppCoordinator
import com.outsidesource.oskitkmp.interactor.Interactor

class HomeViewInteractor(
    private val coordinator: AppCoordinator
): Interactor<Unit>(initialState = Unit) {

    fun appStateExampleButtonClicked() {
        coordinator.appStateExampleClicked()
    }

    fun viewStateExampleButtonClicked() {
        coordinator.viewStateExampleClicked(0)
    }

    fun fileHandlingButtonClicked() {
        coordinator.fileHandlingClicked()
    }

    fun markdownButtonClicked() {
        coordinator.markdownClicked()
    }

    fun popupsButtonClicked() {
        coordinator.popupsClicked()
    }

    fun resourcesButtonClicked() {
        coordinator.resourcesClicked()
    }

    fun iosServicesButtonClicked() {
        coordinator.iosServicesClicked()
    }

    fun widgetsButtonClicked() {
        coordinator.widgetsClicked()
    }

    fun capabilityButtonClicked() {
        coordinator.capabilityClicked()
    }

    fun colorPickerButtonClicked() {
        coordinator.colorPickerClicked()
    }

    fun htmlDemoButtonClicked() {
        coordinator.htmlDemoClicked()
    }
}