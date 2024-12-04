
import com.outsidesource.oskitkmp.storage.IKmpKvStore
import com.outsidesource.oskitkmp.storage.JvmKmpKvStore
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { JvmKmpKvStore(appName = "OSKit-Example-App-KMP") } bind IKmpKvStore::class
}