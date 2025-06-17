import com.outsidesource.oskitkmp.storage.KmpKvStore
import com.outsidesource.oskitkmp.systemui.IKmpSettingsScreenOpener
import com.outsidesource.oskitkmp.systemui.KmpSettingsScreenOpener
import org.koin.dsl.bind
import org.koin.dsl.module
import service.swift.ISwiftExampleService

actual class PlatformContext

private val koin = initKoin(
    platformContext = PlatformContext(),
).koin

actual fun platformModule(platformContext: PlatformContext) = module {
    single { KmpKvStore() }
    single { KmpSettingsScreenOpener() } bind IKmpSettingsScreenOpener::class
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