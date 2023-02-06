package com.outsidesource.oskitExample.composeUI.state.viewStateExample

import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitkmp.interactor.Interactor

data class StateExampleViewState(
    val count: Int = 0,
    val computedCount: Int = 0,
    val validityCheckerValue: String = "",
    val isValidityCheckerValid: Boolean = true,
)

class ViewStateExampleViewInteractor(
    private val coordinator: AppCoordinator,
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

    fun increment() = update { it.copy(count = it.count + 1) }
    fun decrement() = update { it.copy(count = it.count - 1) }
    fun validityCheckerValueChanged(value: String) = update { it.copy(validityCheckerValue = value) }
    fun pushNewScreenButtonClicked(depth: Int) = coordinator.viewStateExample(depth)
    fun popToHome() = coordinator.popToHome()
}