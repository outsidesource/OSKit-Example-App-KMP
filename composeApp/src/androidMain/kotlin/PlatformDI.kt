import android.content.Context
import com.outsidesource.oskitkmp.storage.KmpKvStore
import org.koin.dsl.bind
import org.koin.dsl.module
import service.s3.IS3Service
import service.s3.NoOpS3Service
import service.swift.ISwiftExampleService
import service.swift.NoOpSwiftExampleService

actual data class PlatformContext(val context: Context)

actual fun platformModule(platformContext: PlatformContext) = module {
    single { platformContext.context }
    single { NoOpS3Service() } bind IS3Service::class
    single { NoOpSwiftExampleService() } bind ISwiftExampleService::class
    single { KmpKvStore(appContext = get()) }
}