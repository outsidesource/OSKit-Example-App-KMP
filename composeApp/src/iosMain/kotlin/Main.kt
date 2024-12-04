import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitcompose.uikit.OSComposeUIViewController
import com.outsidesource.oskitkmp.file.IKmpFileHandler
import com.outsidesource.oskitkmp.file.KmpFileHandlerContext
import org.koin.core.component.inject
import platform.UIKit.UIViewController
import ui.app.App

fun MainViewController(): UIViewController {
    val fileHandler by koinInjector.inject<IKmpFileHandler>()

    return OSComposeUIViewController {
        App()
    }.apply {
        fileHandler.init(KmpFileHandlerContext(this))
    }
}