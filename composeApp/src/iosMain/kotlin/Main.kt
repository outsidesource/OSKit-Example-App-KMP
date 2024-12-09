import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitcompose.uikit.OSComposeUIViewController
import com.outsidesource.oskitkmp.filesystem.IKmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import org.koin.core.component.inject
import platform.UIKit.UIViewController
import ui.app.App

fun MainViewController(): UIViewController {
    val fileHandler by koinInjector.inject<IKmpFs>()

    return OSComposeUIViewController {
        App()
    }.apply {
        fileHandler.init(KmpFsContext(this))
    }
}