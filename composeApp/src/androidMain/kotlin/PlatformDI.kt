import com.outsidesource.oskitkmp.storage.AndroidKmpKvStore
import com.outsidesource.oskitkmp.storage.IKmpKvStore
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { AndroidKmpKvStore(appContext = get()) } bind IKmpKvStore::class
}