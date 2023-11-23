package com.outsidesource.oskitExample.common.interactor.app

import com.outsidesource.oskitkmp.interactor.Interactor

data class AppState(
    val isDarkTheme: Boolean = false
)

class AppInteractor : Interactor<AppState>(
    initialState = AppState()
) {

    fun onThemeToggled() {
        update { state -> state.copy(isDarkTheme = !state.isDarkTheme) }
    }
}