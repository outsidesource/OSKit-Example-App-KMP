import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitcompose.uikit.OSComposeUIViewController
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.KmpCapabilityContext
import com.outsidesource.oskitkmp.filesystem.IKmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import org.koin.core.component.inject
import platform.UIKit.UIViewController
import ui.app.App
import kotlin.getValue

fun MainViewController(): UIViewController {
    val fileHandler by koinInjector.inject<IKmpFs>()

    val capabilities by koinInjector.inject<KmpCapabilities>()
    capabilities.init(KmpCapabilityContext())

    return OSComposeUIViewController {
        App()
    }.apply {
        fileHandler.init(KmpFsContext(this))
    }
}