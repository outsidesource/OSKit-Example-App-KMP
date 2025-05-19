
import com.outsidesource.oskitkmp.storage.KmpKvStore
import org.koin.dsl.bind
import com.outsidesource.oskitkmp.systemui.KmpSettingsScreen
import org.koin.dsl.bind
import org.koin.dsl.module
import service.swift.ISwiftExampleService
import service.swift.NoOpSwiftExampleService

actual class PlatformContext

actual fun platformModule(platformContext: PlatformContext) = module {
    single { NoOpSwiftExampleService() } bind ISwiftExampleService::class
    single { KmpKvStore(appName = "OSKit-Example-App") }
    single { KmpSettingsScreen() } bind KmpSettingsScreen::class
}