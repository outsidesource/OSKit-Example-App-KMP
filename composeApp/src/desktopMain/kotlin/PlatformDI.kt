import com.outsidesource.oskitkmp.storage.DesktopKMPStorage
import com.outsidesource.oskitkmp.storage.IKMPStorage
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DesktopKMPStorage(appName = "OSKit-Example-App-KMP") } bind IKMPStorage::class
}