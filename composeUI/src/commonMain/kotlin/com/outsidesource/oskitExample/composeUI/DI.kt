import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitExample.composeUI.platformModule
import com.outsidesource.oskitExample.composeUI.state.app.AppViewInteractor
import org.koin.dsl.module

val composeUIModule = module {
    single { AppCoordinator() }
    single { AppViewInteractor() }
} + platformModule()