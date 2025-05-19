import android.content.Context
import com.outsidesource.oskitkmp.storage.KmpKvStore
import com.outsidesource.oskitkmp.systemui.KmpSettingsScreen
import org.koin.dsl.bind
import org.koin.dsl.module
import service.swift.ISwiftExampleService
import service.swift.NoOpSwiftExampleService

actual data class PlatformContext(val context: Context)

actual fun platformModule(platformContext: PlatformContext) = module {
    single { platformContext.context }
    single { NoOpSwiftExampleService() } bind ISwiftExampleService::class
    single { KmpKvStore(appContext = get()) }
    single { KmpSettingsScreen(context = get()) } bind KmpSettingsScreen::class
}