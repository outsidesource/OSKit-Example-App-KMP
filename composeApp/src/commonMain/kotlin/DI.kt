import com.outsidesource.oskitkmp.filesystem.IKmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFs
import coordinator.AppCoordinator
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.app.AppViewInteractor
import ui.appStateExample.AppStateExampleViewInteractor
import ui.common.ScreenViewInteractor
import ui.device.DeviceHomeViewInteractor
import ui.file.FileHandlingViewInteractor
import ui.home.HomeViewInteractor
import ui.iosServices.IOSServicesScreenViewInteractor
import ui.popups.PopupsScreenViewInteractor
import ui.viewStateExample.ViewStateExampleViewInteractor

val composeAppModule = module {
    single { AppCoordinator() }
    single { KmpFs() } bind IKmpFs::class

    factory { AppViewInteractor(get()) }
    factory { ScreenViewInteractor(get(), get()) }
    factory { HomeViewInteractor(get()) }
    factory { params -> ViewStateExampleViewInteractor(get(), params[0]) }
    factory { AppStateExampleViewInteractor(get(), get()) }
    factory { params -> DeviceHomeViewInteractor(params[0], get()) }
    factory { FileHandlingViewInteractor(get(), get()) }
    factory { PopupsScreenViewInteractor() }
    factory { IOSServicesScreenViewInteractor(get(), get()) }
} + platformModule()