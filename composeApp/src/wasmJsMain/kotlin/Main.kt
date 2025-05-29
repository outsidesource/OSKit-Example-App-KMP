import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitcompose.systemui.KmpAppLifecycleObserver
import com.outsidesource.oskitcompose.systemui.KmpAppLifecycleObserverContext
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.KmpCapabilityContext
import com.outsidesource.oskitkmp.filesystem.KmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import kotlinx.browser.document
import org.jetbrains.compose.resources.configureWebResources
import org.koin.core.component.inject
import ui.app.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(platformContext = PlatformContext,).koin

    configureWebResources {
        resourcePathMapping { path -> "/$path" }
    }

    KmpFs.init(KmpFsContext())

    KmpAppLifecycleObserver.init(KmpAppLifecycleObserverContext())
    val capabilities by koinInjector.inject<KmpCapabilities>()
    capabilities.init(KmpCapabilityContext())

    ComposeViewport(document.body!!) {
        App(deepLink = document.location?.href)
    }
}