import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.composeUI.composeUIModule
import com.outsidesource.oskitExample.composeUI.ui.app.App
import com.outsidesource.oskitcompose.uikit.OSComposeUIViewController
import com.outsidesource.oskitkmp.file.IKMPFileHandler
import com.outsidesource.oskitkmp.file.KMPFileHandlerContext
import com.outsidesource.oskitkmp.outcome.Outcome
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.UIKit.UIViewController

private val koin = initKoin(extraModules = composeUIModule.toTypedArray()).koin

fun MainViewController(): UIViewController {
    val fileHandler by koin.inject<IKMPFileHandler>()

    return OSComposeUIViewController {
        App()
    }.apply {
        fileHandler.init(KMPFileHandlerContext(this))
    }
}

fun initKoin(testInterface: ITestInterface) {
    koin.loadModules(
        listOf(
            module {
                single { testInterface } bind ITestInterface::class
            }
        )
    )
}

interface ITestInterface {
    fun test1(): String
    fun test2(): Outcome<List<Foo>, Exception>
    fun test3(): Outcome<String, Exception>
    fun test4(): Flow<String>
    suspend fun test5(flow: Flow<String>)
}

data class Foo(
    val one: String,
    val two: Int,
)