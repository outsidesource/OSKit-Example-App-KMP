import com.outsidesource.oskitkmp.storage.IKmpKvStore
import com.outsidesource.oskitkmp.storage.WasmKmpKvStore
import com.outsidesource.oskitkmp.storage.WasmKmpKvStoreType
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { WasmKmpKvStore(type = WasmKmpKvStoreType.LocalStorage) } bind IKmpKvStore::class
}