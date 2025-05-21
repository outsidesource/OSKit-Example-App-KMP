import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitcompose.systemui.KmpAppLifecycleObserver
import com.outsidesource.oskitcompose.systemui.KmpAppLifecycleObserverContext
import com.outsidesource.oskitcompose.uikit.OSComposeUIViewController
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.KmpCapabilityContext
import com.outsidesource.oskitkmp.filesystem.KmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import org.koin.core.component.inject
import platform.UIKit.UIViewController
import ui.app.App

fun MainViewController(): UIViewController {
    KmpAppLifecycleObserver.init(KmpAppLifecycleObserverContext())
    val capabilities by koinInjector.inject<KmpCapabilities>()
    capabilities.init(KmpCapabilityContext())

    return OSComposeUIViewController {
        App()
    }.apply {
        KmpFs.init(KmpFsContext(this))
    }
}