package ui.openForResultExample

import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.outcome.unwrapOrNull
import coordinator.AppCoordinator
import kotlinx.coroutines.launch

data class OpenForResultExampleViewState(
    val result: Boolean? = null,
)

class OpenForResultExampleViewInteractor(
    private val coordinator: AppCoordinator
): Interactor<OpenForResultExampleViewState>(
    initialState = OpenForResultExampleViewState()
) {

    fun openConfirmationRouteClicked() {
        interactorScope.launch {
            val res = coordinator.openForResultExampleConfirmationClicked().unwrapOrNull()
            update { state -> state.copy(result = res) }
        }
    }
}