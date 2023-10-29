import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.composeUI.composeUIModule
import com.outsidesource.oskitExample.composeUI.ui.app.App
import com.outsidesource.oskitcompose.uikit.OSComposeUIViewController
import com.outsidesource.oskitkmp.file.IKMPFileHandler
import com.outsidesource.oskitkmp.file.KMPFileHandlerContext
import platform.UIKit.*

private val koin = initKoin(extraModules = composeUIModule.toTypedArray()).koin

fun MainViewController(): UIViewController {
    val fileHandler by koin.inject<IKMPFileHandler>()

    return OSComposeUIViewController {
        App()
    }.apply {
        fileHandler.init(KMPFileHandlerContext(this))
    }
}



