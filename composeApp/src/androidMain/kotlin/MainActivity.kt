import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import ui.app.App
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.file.KMPFileHandler
import com.outsidesource.oskitkmp.file.KMPFileHandlerContext
import org.koin.core.component.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val fileHandler by koinInjector.inject<KMPFileHandler>()
        fileHandler.init(KMPFileHandlerContext(application, this))

        setContent {
            App()
        }
    }
}