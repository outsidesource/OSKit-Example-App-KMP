package ui.viewStateExample

import com.outsidesource.oskitkmp.interactor.Interactor
import coordinator.AppCoordinator

data class StateExampleViewState(
    val count: Int = 0,
    val computedCount: Int = 0,
    val validityCheckerValue: String = "",
    val isValidityCheckerValid: Boolean = true,
)

class ViewStateExampleViewInteractor(
    private val coordinator: AppCoordinator,
    private val depth: Int,
): Interactor<StateExampleViewState>(
    initialState = StateExampleViewState()
) {
    private val numberRegex = Regex("^[0-9]*$")

    override fun computed(state: StateExampleViewState): StateExampleViewState {
        return state.copy(
            computedCount = state.count * 2,
            isValidityCheckerValid = state.validityCheckerValue.matches(numberRegex)
        )
    }

    fun incrementClicked() = update { state -> state.copy(count = state.count + 1) }
    fun decrementClicked() = update { state -> state.copy(count = state.count - 1) }
    fun validityCheckerValueChanged(value: String) = update { state -> state.copy(validityCheckerValue = value) }
    fun pushNewScreenButtonClicked(depth: Int) = coordinator.viewStateExampleClicked(depth)
    fun popToHome() = coordinator.popToHome()
}