import com.outsidesource.oskitkmp.storage.KmpKvStore
import com.outsidesource.oskitkmp.storage.WasmKmpKvStoreType
import com.outsidesource.oskitkmp.systemui.KmpSettingsScreen
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { KmpKvStore(type = WasmKmpKvStoreType.LocalStorage) }
    single { KmpSettingsScreen() } bind KmpSettingsScreen::class
}