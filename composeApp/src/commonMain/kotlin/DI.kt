import com.outsidesource.oskitkmp.capability.BluetoothCapabilityFlags
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.LocationCapabilityFlags
import com.outsidesource.oskitkmp.filesystem.KmpFs
import coordinator.AppCoordinator
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

val composeAppModule = module {
    single { AppCoordinator() }
    single {
        KmpCapabilities(
            bluetoothFlags = BluetoothCapabilityFlags.entries.toTypedArray(),
            locationFlags = LocationCapabilityFlags.entries.toTypedArray(),
        )
    }

    factory { params -> AppViewInteractor(params[0], get(), get()) }
    factory { ScreenViewInteractor(get(), get()) }
    factory { HomeViewInteractor(get()) }
    factory { params -> ViewStateExampleViewInteractor(get(), params[0]) }
    factory { AppStateExampleViewInteractor(get(), get()) }
    factory { params -> DeviceHomeViewInteractor(params[0], get()) }
    factory { FileSystemViewInteractor(KmpFs.External, KmpFs.Internal, get()) }
    factory { PopupsScreenViewInteractor() }
    factory { IOSServicesScreenViewInteractor(get(), get()) }
    factory { CapabilityScreenViewInteractor(get()) }
    factory { ColorPickerViewInteractor() }
} + platformModule()