import android.content.Context
import com.outsidesource.oskitkmp.storage.KmpKvStore
import com.outsidesource.oskitkmp.systemui.IKmpSettingsScreenOpener
import com.outsidesource.oskitkmp.systemui.KmpSettingsScreenOpener
import org.koin.dsl.bind
import org.koin.dsl.module
import service.swift.ISwiftExampleService
import service.swift.NoOpSwiftExampleService

actual data class PlatformContext(val context: Context)

actual fun platformModule(platformContext: PlatformContext) = module {
    single { platformContext.context }
    single { NoOpSwiftExampleService() } bind ISwiftExampleService::class
    single { KmpKvStore(appContext = get()) }
    single { KmpSettingsScreenOpener(context = get()) } bind IKmpSettingsScreenOpener::class
}