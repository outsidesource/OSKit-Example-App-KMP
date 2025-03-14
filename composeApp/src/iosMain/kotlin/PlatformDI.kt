import com.outsidesource.oskitkmp.storage.KmpKvStore
import org.koin.dsl.bind
import org.koin.dsl.module
import service.s3.IS3Service
import service.swift.ISwiftExampleService

actual class PlatformContext

private val koin = initKoin(
    platformContext = PlatformContext(),
).koin

actual fun platformModule(platformContext: PlatformContext) = module {
    single { KmpKvStore() }
}

fun loadKoinSwiftModules(
    swiftExampleService: ISwiftExampleService,
    s3Service: IS3Service,
) {
    koin.loadModules(
        listOf(
            module {
                single { swiftExampleService } bind ISwiftExampleService::class
                single { s3Service } bind IS3Service::class
            }
        )
    )
}