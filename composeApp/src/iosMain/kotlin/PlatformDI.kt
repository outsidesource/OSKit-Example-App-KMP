import service.IOSS3Service
import service.s3.IS3Service
import service.swift.ISwiftExampleService
import com.outsidesource.oskitkmp.storage.KmpKvStore
import org.koin.dsl.bind
import org.koin.dsl.module

actual class PlatformContext

private val koin = initKoin(
    platformContext = PlatformContext(),
).koin

actual fun platformModule(platformContext: PlatformContext) = module {
    single { IOSS3Service() } bind IS3Service::class
    single { KmpKvStore() }
}

fun loadKoinSwiftModules(swiftExampleService: ISwiftExampleService) {
    koin.loadModules(
        listOf(
            module {
                single { swiftExampleService } bind ISwiftExampleService::class
            }
        )
    )
}