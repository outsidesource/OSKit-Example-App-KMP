import com.outsidesource.oskitkmp.storage.IKMPStorage
import com.outsidesource.oskitkmp.storage.InMemoryKMPStorage
import com.outsidesource.oskitkmp.storage.WASMKMPStorage
import com.outsidesource.oskitkmp.storage.WASMKMPStorageType
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { WASMKMPStorage(type = WASMKMPStorageType.LocalStorage) } bind IKMPStorage::class
}