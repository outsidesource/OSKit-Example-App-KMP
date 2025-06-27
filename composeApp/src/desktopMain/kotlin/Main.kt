import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitcompose.systemui.KmpAppLifecycleObserver
import com.outsidesource.oskitcompose.systemui.KmpAppLifecycleObserverContext
import com.outsidesource.oskitcompose.systemui.KmpWindowInsetsHolder
import com.outsidesource.oskitcompose.systemui.LocalKmpWindowInsets
import com.outsidesource.oskitcompose.window.SizedWindow
import com.outsidesource.oskitcompose.window.rememberPersistedWindowState
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.KmpCapabilityContext
import com.outsidesource.oskitkmp.filesystem.KmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import org.koin.core.component.inject
import ui.app.App
import java.awt.Dimension

private val koin = initKoin(
    platformContext = PlatformContext(),
).koin

fun main() = application {
    val windowState = rememberPersistedWindowState("OSKit-Example-App", initialSize = Dimension(800, 600))

    val capabilities by koinInjector.inject<KmpCapabilities>()
    capabilities.init(KmpCapabilityContext())

    SizedWindow(
        title = "OSKit-KMP Example",
        onCloseRequest = ::exitApplication,
        minWindowSize = Dimension(300, 300),
        state = windowState,
    ) {
        DisposableEffect(Unit) {
            KmpAppLifecycleObserver.init(KmpAppLifecycleObserverContext(this@SizedWindow.window))
            KmpFs.init(KmpFsContext(window = this@SizedWindow.window, appName = "OSKit-Example-App"))
            onDispose {  }
        }

        CompositionLocalProvider(LocalKmpWindowInsets provides KmpWindowInsetsHolder(bottom = 16.dp)) {
            App()
        }
    }
}