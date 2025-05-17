import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeViewport
import com.outsidesource.oskitcompose.html.Html
import com.outsidesource.oskitcompose.html.rememberHtmlState
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.KmpCapabilityContext
import com.outsidesource.oskitkmp.filesystem.KmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.configureWebResources
import org.koin.core.component.inject
import org.w3c.dom.*

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(platformContext = PlatformContext,).koin

    configureWebResources {
        resourcePathMapping { path -> "/$path" }
    }

    KmpFs.init(KmpFsContext())

    val capabilities by koinInjector.inject<KmpCapabilities>()
    capabilities.init(KmpCapabilityContext())

    ComposeViewport(document.body!!) {
        var tab by remember { mutableStateOf(Page.One) }

        Column(
            modifier = Modifier.clickable {
                println("Clicked!")
            }.onKeyEvent {
                println("Hello: ${it.key}")
                false
            }
        ) {
            Row {
                Button(onClick = { tab = Page.One }) { Text("One") }
                Button(onClick = { tab = Page.Two }) { Text("Two") }
            }
            AnimatedContent(
                targetState = tab,
                transitionSpec = {
                    scaleIn(tween(1000)) +
                            fadeIn(tween(1000)) togetherWith
                            scaleOut(tween(1000)) +
                            fadeOut(tween(1000))
                }
            )
            {
                when (it) {
                    Page.One -> PageOne(if (tab == Page.One) "in" else "out")
                    Page.Two -> PageTwo(if (tab == Page.Two) "in" else "out")
                }
            }
        }
//        App(deepLink = document.location?.href)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PageOne(direction: String) {
    val htmlState = rememberHtmlState()
    val alphaAnim = remember { Animatable(0f) }
    LaunchedEffect(direction) {
        alphaAnim.animateTo(if (direction == "in") 1f else 0f, tween(1000))
    }

    LaunchedEffect(alphaAnim) {
        snapshotFlow { alphaAnim.value }.collect {
            htmlState.content.style.opacity = it.toString()
            htmlState.content.style.transformOrigin = "top left"
            htmlState.content.style.unsafeCast<CSSStyleDeclarationExt>().scale = it.toString()
        }
    }

    LaunchedEffect(Unit) {
        var count = 0
        while (true) {
            delay(100)
            count++
            htmlState.content.querySelector(".count")?.innerHTML = "$count"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(Color.Blue)
        )
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
            ) {
                Button(onClick = {}) { Text("Test")}
            }
            Html(
                modifier = Modifier.fillMaxWidth(),
                state = htmlState,
                inlineJs = {
                    """
                    import { Env } from "${htmlState.runtimeJsUrl}"
                    
                    function test() {
                        console.log("Hello!")
                        Env.emit(new CustomEvent("hello"))
                    }

                    Env.content.querySelector("button").addEventListener("click", test)
                    """
                },
                html = {
                    """
                    <iframe width="100%" style="max-width: 500px; aspect-ratio: 16/9;" src="https://www.youtube.com/embed/0NDqYZVbpho?si=_tjA9VpXKSHiY1hy" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
                    <div style="width: 100%; height: 100%; background: green;">
                        <div>Hello <button>Click</button> <div class="count">0</div></div>
                        <input type="text" />
                        <ul>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                            <li>One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One One </li>
                        </ul>
                    </div>
                    """
                },
            )
            Box(
                modifier = Modifier
                    .height(1000.dp)
                    .fillMaxWidth()
                    .background(Color.Gray)
            ) {
                Button(onClick = {}) { Text("Test")}
            }
        }
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(Color.Blue)
        )
    }
}

// TODO: Demos Google Maps, Youtube embed, charts, react app?, video, audio, iframe

@Composable
fun PageTwo(direction: String) {
    val htmlState = rememberHtmlState()
    val alphaAnim = remember { Animatable(0f) }
    LaunchedEffect(direction) {
        alphaAnim.animateTo(if (direction == "in") 1f else 0f, tween(1000))
    }

    LaunchedEffect(alphaAnim) {
        snapshotFlow { alphaAnim.value }.collect {
            htmlState.content.style.opacity = it.toString()
            htmlState.content.style.transformOrigin = "top left"
            htmlState.content.style.unsafeCast<CSSStyleDeclarationExt>().scale = it.toString()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(Color.Blue)
        )
        Html(
            state = htmlState,
            modifier = Modifier.fillMaxWidth().weight(1f),
            html = {
                """
                <div style="width: 100%; height: 100%; background: green;">
                    Hello <button>Click</button>
                </div>
                """
            },
        )
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(Color.Blue)
        )
    }
}

enum class Page {
    One,
    Two,
}

private external interface CSSStyleDeclarationExt : JsAny {
    var scale: String
    var translate: String
}