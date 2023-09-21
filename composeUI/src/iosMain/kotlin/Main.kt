import androidx.compose.ui.window.ComposeUIViewController
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.composeUI.composeUIModule
import com.outsidesource.oskitExample.composeUI.ui.app.App
import com.outsidesource.oskitkmp.file.IKMPFileHandler
import com.outsidesource.oskitkmp.file.KMPFileHandlerContext
import platform.UIKit.UIViewController

private val koin = initKoin(extraModules = composeUIModule.toTypedArray()).koin

fun MainViewController(): UIViewController {
    val fileHandler by koin.inject<IKMPFileHandler>()

    return ComposeUIViewController {
        App()
    }.apply {
        fileHandler.init(KMPFileHandlerContext(this))
    }
}