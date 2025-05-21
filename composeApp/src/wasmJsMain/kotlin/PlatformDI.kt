import com.outsidesource.oskitkmp.storage.KmpKvStore
import com.outsidesource.oskitkmp.storage.WasmKmpKvStoreType
import org.koin.dsl.bind
import org.koin.dsl.module
import service.swift.ISwiftExampleService
import service.swift.NoOpSwiftExampleService

actual object PlatformContext

actual fun platformModule(platformContext: PlatformContext) = module {
    single { NoOpSwiftExampleService() } bind ISwiftExampleService::class
    single { KmpKvStore(type = WasmKmpKvStoreType.LocalStorage) }
}