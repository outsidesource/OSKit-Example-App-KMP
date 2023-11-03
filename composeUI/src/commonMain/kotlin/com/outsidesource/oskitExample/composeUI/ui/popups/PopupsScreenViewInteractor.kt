package com.outsidesource.oskitExample.composeUI.ui.popups

import com.outsidesource.oskitkmp.interactor.Interactor

data class PopupsScreenState(
    val isBottomSheetVisible: Boolean = false,
    val isDrawerVisible: Boolean = false,
    val isModalVisible: Boolean = false,
    val isPopoverVisible: Boolean = false,
)

class PopupsScreenViewInteractor : Interactor<PopupsScreenState>(
    initialState = PopupsScreenState(),
) {
    fun modalButtonClicked() {
        update { state -> state.copy(isModalVisible = true) }
    }

    fun modalDismissed() {
        update { state -> state.copy(isModalVisible = false) }
    }

    fun bottomSheetButtonClicked() {
        update { state -> state.copy(isBottomSheetVisible = true) }
    }

    fun bottomSheetDismissed() {
        update { state -> state.copy(isBottomSheetVisible = false) }
    }

    fun drawerButtonClicked() {
        update { state -> state.copy(isDrawerVisible = true) }
    }

    fun drawerDismissed() {
        update { state -> state.copy(isDrawerVisible = false) }
    }

    fun popoverButtonClicked() {
        update { state -> state.copy(isPopoverVisible = true) }
    }

    fun popoverDismissed() {
        update { state -> state.copy(isPopoverVisible = false) }
    }
}