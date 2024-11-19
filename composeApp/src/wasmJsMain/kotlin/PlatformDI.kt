import com.outsidesource.oskitkmp.storage.IKMPStorage
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { InMemoryKMPStorage() } bind IKMPStorage::class
}