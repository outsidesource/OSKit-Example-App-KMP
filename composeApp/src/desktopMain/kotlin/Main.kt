import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import com.outsidesource.oskitExample.common.PlatformContext
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitcompose.systemui.KMPWindowInsetsHolder
import com.outsidesource.oskitcompose.systemui.LocalKMPWindowInsets
import com.outsidesource.oskitcompose.window.SizedWindow
import com.outsidesource.oskitcompose.window.rememberPersistedWindowState
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.KmpCapabilityContext
import com.outsidesource.oskitkmp.filesystem.IKmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import org.koin.core.component.inject
import ui.app.App
import java.awt.Dimension
import kotlin.getValue

private val koin = initKoin(
    platformContext = PlatformContext(),
    extraModules = composeAppModule.toTypedArray()
).koin

fun main() = application {
    val fileHandler by koinInjector.inject<IKmpFs>()
    val windowState = rememberPersistedWindowState("OSKit-KMP-Example", initialSize = Dimension(800, 600))

    val capabilities by koinInjector.inject<KmpCapabilities>()
    capabilities.init(KmpCapabilityContext())

    SizedWindow(
        title = "OSKit-KMP Example",
        onCloseRequest = ::exitApplication,
        minWindowSize = Dimension(800, 600),
        state = windowState,
    ) {
        DisposableEffect(Unit) {
            fileHandler.init(KmpFsContext(this@SizedWindow.window))
            onDispose {  }
        }

        CompositionLocalProvider(LocalKMPWindowInsets provides KMPWindowInsetsHolder(bottom = 16.dp)) {
            App()
        }
    }
}