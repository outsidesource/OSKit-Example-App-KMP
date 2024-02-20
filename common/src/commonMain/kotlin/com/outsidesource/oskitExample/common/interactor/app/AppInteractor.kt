package com.outsidesource.oskitExample.common.interactor.app

import com.outsidesource.oskitExample.common.service.preferences.AppColorTheme
import com.outsidesource.oskitExample.common.service.preferences.IPreferencesService
import com.outsidesource.oskitkmp.interactor.Interactor
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class AppState(
    val isDarkTheme: Boolean = false
)

class AppInteractor(
    private val preferencesService: IPreferencesService,
) : Interactor<AppState>(
    initialState = AppState(
        isDarkTheme = runBlocking { preferencesService.getTheme() } == AppColorTheme.Dark
    )
) {

    fun onThemeToggled() {
        interactorScope.launch {
            update { state -> state.copy(isDarkTheme = !state.isDarkTheme) }
            preferencesService.setTheme(if (state.isDarkTheme) AppColorTheme.Dark else AppColorTheme.Light)
        }
    }
}