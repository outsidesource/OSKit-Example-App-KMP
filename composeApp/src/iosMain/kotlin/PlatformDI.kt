import com.outsidesource.oskitExample.common.PlatformContext
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.common.service.IOSS3Service
import com.outsidesource.oskitExample.common.service.s3.IS3Service
import com.outsidesource.oskitExample.common.service.swift.ISwiftExampleService
import org.koin.dsl.bind
import com.outsidesource.oskitkmp.storage.KmpKvStore
import com.outsidesource.oskitkmp.systemui.KmpSettingsScreen
import org.koin.dsl.module

private val koin = initKoin(
    platformContext = PlatformContext(),
    extraModules = composeAppModule.toTypedArray()
).koin

actual fun platformModule() = module {
    single { IOSS3Service() } bind IS3Service::class
    single { KmpKvStore() }
    single { KmpSettingsScreen() } bind KmpSettingsScreen::class
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