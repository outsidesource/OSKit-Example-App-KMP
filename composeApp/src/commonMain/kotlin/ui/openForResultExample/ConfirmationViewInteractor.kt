package ui.openForResultExample

import com.outsidesource.oskitkmp.interactor.Interactor
import coordinator.AppCoordinator

data class ConfirmationViewState(
    val result: Boolean? = null,
)

class ConfirmationViewInteractor(
    private val coordinator: AppCoordinator
): Interactor<ConfirmationViewState>(
    initialState = ConfirmationViewState()
) {

    fun yesClicked() {
        coordinator.openForResultExampleConfirmationYesClicked()
    }

    fun noClicked() {
        coordinator.openForResultExampleConfirmationNoClicked()
    }
}