import com.outsidesource.oskitkmp.storage.KmpKvStore
import com.outsidesource.oskitkmp.storage.WasmKmpKvStoreType
import org.koin.dsl.module

actual fun platformModule() = module {
    single { KmpKvStore(type = WasmKmpKvStoreType.LocalStorage) }
}