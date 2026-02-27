import com.outsidesource.oskitkmp.storage.KmpKvStore
import org.koin.dsl.bind
import org.koin.dsl.module
import service.swift.ISwiftExampleService

actual class PlatformContext

val koin = initKoin(
    platformContext = PlatformContext(),
).koin

actual fun platformModule(platformContext: PlatformContext) = module {
    single { KmpKvStore() }
}

fun loadKoinSwiftModules(
    swiftExampleService: ISwiftExampleService,
) {
    koin.loadModules(
        listOf(
            module {
                single { swiftExampleService } bind ISwiftExampleService::class
            }
        )
    )
}