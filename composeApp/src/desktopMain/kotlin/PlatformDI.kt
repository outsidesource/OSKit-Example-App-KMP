
import com.outsidesource.oskitkmp.storage.KmpKvStore
import org.koin.dsl.module

actual fun platformModule() = module {
    single { KmpKvStore(appName = "OSKit-Example-App-KMP") }
}