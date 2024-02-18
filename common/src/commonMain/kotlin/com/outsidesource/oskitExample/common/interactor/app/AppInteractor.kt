package com.outsidesource.oskitExample.common.interactor.app

import com.outsidesource.oskitExample.common.service.preferences.AppTheme
import com.outsidesource.oskitExample.common.service.preferences.IPreferencesService
import com.outsidesource.oskitkmp.interactor.Interactor

data class AppState(
    val isDarkTheme: Boolean = false
)

class AppInteractor(
    private val preferencesService: IPreferencesService,
) : Interactor<AppState>(
    initialState = AppState(
        isDarkTheme = preferencesService.getTheme() == AppTheme.Dark
    )
) {

    fun onThemeToggled() {
        update { state -> state.copy(isDarkTheme = !state.isDarkTheme) }
        preferencesService.setTheme(if (state.isDarkTheme) AppTheme.Dark else AppTheme.Light)
    }
}