
import com.outsidesource.oskitkmp.storage.KmpKvStore
import com.outsidesource.oskitkmp.systemui.IKmpSettingsScreenOpener
import com.outsidesource.oskitkmp.systemui.KmpSettingsScreenOpener
import org.koin.dsl.bind
import org.koin.dsl.module
import service.swift.ISwiftExampleService
import service.swift.NoOpSwiftExampleService

actual class PlatformContext

actual fun platformModule(platformContext: PlatformContext) = module {
    single { NoOpSwiftExampleService() } bind ISwiftExampleService::class
    single { KmpKvStore(appName = "OSKit-Example-App") }
    single { KmpSettingsScreenOpener() } bind IKmpSettingsScreenOpener::class
}