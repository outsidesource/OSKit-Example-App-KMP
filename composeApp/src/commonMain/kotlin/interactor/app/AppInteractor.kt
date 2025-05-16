package interactor.app

import service.preferences.AppColorTheme
import service.preferences.IPreferencesService
import com.outsidesource.oskitkmp.interactor.Interactor
import kotlinx.coroutines.launch

data class AppState(
    val isDarkTheme: Boolean = false
)

class AppInteractor(
    private val preferencesService: IPreferencesService,
) : Interactor<AppState>(
    initialState = AppState(
        isDarkTheme = false
    )
) {

    init {
        interactorScope.launch {
            val theme = preferencesService.getTheme()
            update { state ->
                state.copy(isDarkTheme = theme == AppColorTheme.Dark)
            }
        }
    }

    fun onThemeToggled() {
        interactorScope.launch {
            update { state -> state.copy(isDarkTheme = !state.isDarkTheme) }
            preferencesService.setTheme(if (state.isDarkTheme) AppColorTheme.Dark else AppColorTheme.Light)
        }
    }
}