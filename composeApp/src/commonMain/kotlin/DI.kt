import coordinator.AppCoordinator
import ui.app.AppViewInteractor
import ui.appStateExample.AppStateExampleViewInteractor
import ui.common.ScreenViewInteractor
import ui.device.DeviceHomeViewInteractor
import ui.file.FileHandlingViewInteractor
import ui.home.HomeViewInteractor
import ui.iosServices.IOSServicesScreenViewInteractor
import ui.popups.PopupsScreenViewInteractor
import ui.viewStateExample.ViewStateExampleViewInteractor
import com.outsidesource.oskitkmp.file.IKmpFileHandler
import com.outsidesource.oskitkmp.file.KmpFileHandler
import org.koin.dsl.bind
import org.koin.dsl.module

val composeAppModule = module {
    single { AppCoordinator() }
    single { KmpFileHandler() } bind IKmpFileHandler::class

    factory { AppViewInteractor(get()) }
    factory { ScreenViewInteractor(get(), get()) }
    factory { HomeViewInteractor(get()) }
    factory { ViewStateExampleViewInteractor(get()) }
    factory { AppStateExampleViewInteractor(get(), get()) }
    factory { params -> DeviceHomeViewInteractor(params[0], get()) }
    factory { FileHandlingViewInteractor(get(), get()) }
    factory { PopupsScreenViewInteractor() }
    factory { IOSServicesScreenViewInteractor(get(), get()) }
} + platformModule()