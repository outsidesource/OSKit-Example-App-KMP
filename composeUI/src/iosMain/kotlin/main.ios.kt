import androidx.compose.ui.window.ComposeUIViewController
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.composeUI.composeUIModule
import com.outsidesource.oskitExample.composeUI.ui.app.App

private val koin = initKoin(extraModules = composeUIModule.toTypedArray()).koin

fun MainViewController() = ComposeUIViewController { App() }