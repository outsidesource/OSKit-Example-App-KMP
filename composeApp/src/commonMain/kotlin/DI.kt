import com.outsidesource.oskitkmp.capability.BluetoothCapabilityFlags
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.LocationCapabilityFlags
import com.outsidesource.oskitkmp.capability.StorageCapabilityFlags
import com.outsidesource.oskitkmp.filesystem.KmpFs
import coordinator.AppCoordinator
import interactor.app.AppInteractor
import service.device.DeviceService
import interactor.device.DeviceInteractor
import service.preferences.IPreferencesService
import service.preferences.PreferencesService
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.app.AppViewInteractor
import ui.appStateExample.AppStateExampleViewInteractor
import ui.capability.CapabilityScreenViewInteractor
import ui.colorPicker.ColorPickerViewInteractor
import ui.common.ScreenViewInteractor
import ui.device.DeviceHomeViewInteractor
import ui.file.FileSystemViewInteractor
import ui.home.HomeViewInteractor
import ui.iosServices.IOSServicesScreenViewInteractor
import ui.popups.PopupsScreenViewInteractor
import ui.viewStateExample.ViewStateExampleViewInteractor

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
    platformContext: PlatformContext,
    extraModules: Array<Module> = emptyArray()
) = startKoin {
    appDeclaration()
    modules(commonModule(), platformModule(platformContext), *extraModules)
}

fun commonModule() = module {
    single { DeviceService() }
    single { PreferencesService(get()) } bind IPreferencesService::class
    single {
        KmpCapabilities(
            bluetoothFlags = BluetoothCapabilityFlags.entries.toTypedArray(),
            locationFlags = LocationCapabilityFlags.entries.toTypedArray(),
            storageFlags = StorageCapabilityFlags.entries.toTypedArray(),
        )
    }
    single { AppCoordinator() }

    single { DeviceInteractor(get()) }
    single { AppInteractor(get()) }

    factory { params -> AppViewInteractor(params[0], get(), get()) }
    factory { ScreenViewInteractor(get(), get()) }
    factory { HomeViewInteractor(get()) }
    factory { params -> ViewStateExampleViewInteractor(get(), params[0]) }
    factory { AppStateExampleViewInteractor(get(), get()) }
    factory { params -> DeviceHomeViewInteractor(params[0], get()) }
    factory { FileSystemViewInteractor(KmpFs.External, KmpFs.Internal, get()) }
    factory { PopupsScreenViewInteractor() }
    factory { IOSServicesScreenViewInteractor(get()) }
    factory { CapabilityScreenViewInteractor(get()) }
    factory { ColorPickerViewInteractor() }
}