import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeViewport
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.KmpCapabilityContext
import com.outsidesource.oskitkmp.filesystem.KmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import kotlinx.browser.document
import org.jetbrains.compose.resources.configureWebResources
import org.koin.core.component.inject
import org.w3c.dom.CustomEvent
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLScriptElement
import org.w3c.dom.OPEN
import org.w3c.dom.ShadowRootInit
import org.w3c.dom.ShadowRootMode
import kotlin.math.roundToInt
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
//                    scaleIn(tween(1000)) +
                            fadeIn(tween(1000)) togetherWith
//                            scaleOut(tween(1000)) +
                            fadeOut(tween(1000))
                }
            )
            {
                when (it) {
                    Page.One -> PageOne()
                    Page.Two -> PageTwo()
                }
            }
        }
//        App(deepLink = document.location?.href)
    }
}

@Composable
fun PageOne() {
    Column(modifier = Modifier.fillMaxSize()) {
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
                    .height(1000.dp)
                    .fillMaxWidth()
                    .background(Color.Gray)
            ) {
                TextField(value = "", onValueChange = {})
            }
            Html(
                modifier = Modifier.fillMaxWidth(),
                script = {
                    """
                        function test() {
                            console.log("Hello!")
                            OsKit.sendEvent(new CustomEvent("hello"))
                        }
                        
                        OsKit.content.querySelector("button").addEventListener("click", test)
                        """
                },
                html = {
                    """
                        <div style="width: 100%; height: 100%; background: green;">
                            <div>Hello <button>Click</button></div>
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
            )
        }
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(Color.Blue)
        )
    }
}

@Composable
fun PageTwo() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(Color.Blue)
        )
        Html(
            modifier = Modifier.fillMaxWidth().height(300.dp),
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

external class ResizeEventDetail : JsAny {
    val width: JsNumber
    val height: JsNumber
}

/**
 * Limitations:
 *   1. Alpha and scale graphic transformations must be applied directly to the HTML DOM elements.
 *      This affects animations or any parent graphic transformations using alpha/scale.
 *   2. Accessibility will not flow naturally as the DOM elements are outside the canvas.
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
fun Html(
    modifier: Modifier = Modifier,
    script: (() -> String)? = null,
    html: () -> String,
) {
    val density = LocalDensity.current
    val container = remember(Unit) { document.createElement("div") as HTMLDivElement }
    val content = remember(Unit) { document.createElement("div") as HTMLDivElement }
    val scriptElement = remember(script) { document.createElement("script") as HTMLScriptElement }
    var width by remember(Unit) { mutableStateOf(0.dp) }
    var height by remember(Unit) { mutableStateOf(0.dp) }

    DisposableEffect(Unit) {
        val shadowRoot = container.attachShadow(ShadowRootInit(ShadowRootMode.OPEN))
        container.className = "oskit-${Uuid.random().toHexString()}"
        container.style.position = "absolute"
        container.style.top = "0"
        container.style.left = "0"
        container.style.zIndex = "1"
        container.style.overflowX = "hidden"
        container.style.overflowY = "hidden"

        content.className = "oskit-${Uuid.random().toHexString()}"
        content.innerHTML = html()
        shadowRoot.appendChild(content)

        // TODO: The event proxy and resize observer need to be added regardless of if there is a user provided script
        if (script != null) {
            container.addEventListener("oskit-resize") {
                width = ((it as CustomEvent).detail?.unsafeCast<ResizeEventDetail>())?.width?.toInt()?.dp ?: 0.dp
                height = ((it as CustomEvent).detail?.unsafeCast<ResizeEventDetail>())?.height?.toInt()?.dp ?: 0.dp
            }
            scriptElement.type = "module"
            scriptElement.textContent = """
                const OsKit = (() => {
                    const canvas = document.querySelector("canvas")
                    const container = document.querySelector(".${container.className}")
                    const content = container.shadowRoot.querySelector(".${content.className}")

                    // Event proxy
                    container.addEventListener("wheel", (ev) => canvas.dispatchEvent(new WheelEvent("wheel", ev)))
                    const pointerEvents = ["pointerover", "pointerenter", "pointerdown", "pointermove", "pointerup", "pointercancel", "pointerout", "pointerleave", "gotpointercapture", "lostpointercapture"]
                    pointerEvents.forEach((type) => container.addEventListener(type, (ev) => canvas.dispatchEvent(new PointerEvent(type, ev))))
                    const touchEvents = ["touchstart", "touchend", "touchmove", "touchcancel"]
                    touchEvents.forEach((type) => container.addEventListener(type, (ev) => canvas.dispatchEvent(new TouchEvent(type, ev))))
                    const keyboardEvents = ["keydown", "keyup", "keypress"]
                    keyboardEvents.forEach((type) => container.addEventListener(type, (ev) => canvas.dispatchEvent(new KeyboardEvent(type, ev))))
                    const mouseEvents = ["click", "dblclick", "mousedown", "mouseenter", "mouseleave", "mouseout", "mouseover", "mouseup"]
                    mouseEvents.forEach((type) => container.addEventListener(type, (ev) => canvas.dispatchEvent(new MouseEvent(type, ev))))
                    
                    const observer = new ResizeObserver((ev) => {
                        console.log(content.offsetWidth)
                        container.dispatchEvent(new CustomEvent("oskit-resize", {detail: {height: content.scrollHeight, width: content.scrollWidth}}))
                    })
                    
                    observer.observe(content)
                
                    return Object.freeze({
                        container: container,
                        content: content,
                        sendEvent: (ev) => container.dispatchEvent(ev),   
                    })
                })();
            """.trimIndent() + script().trimIndent()
            shadowRoot.appendChild(scriptElement)
        }

        document.body?.appendChild(container)

        onDispose { document.body?.removeChild(container) }
    }

    Layout(
        modifier = modifier.onGloballyPositioned { layoutCoordinates ->
            val bounds = layoutCoordinates.boundsInRoot().let { Rect(it.topLeft / density.density, it.size / density.density) }
            val rootPos = layoutCoordinates.positionInRoot() / density.density
            val size = layoutCoordinates.size.let { Size(it.width / density.density, it.height / density.density) }

            container.style.height = "${bounds.height}px"
            container.style.width = "${bounds.width}px"
            container.style.transform = "translate(${bounds.left}px, ${bounds.top}px)"

//            content.style.width = "${size.width}px"
//            content.style.height = "${size.height}px"
            val top = if (bounds.height < size.height && rootPos.y < bounds.top) rootPos.y - bounds.top else 0
            val left = if (bounds.width < size.width && rootPos.x < bounds.left) rootPos.x - bounds.left else 0
            content.style.transform = "translate(${left}px, ${top}px)"
        }
    ) { _, constraints ->
        val width = if (constraints.hasFixedHeight || constraints.hasBoundedHeight) constraints.minWidth else width.toPx().roundToInt()
        val height = if (constraints.hasFixedHeight || constraints.hasBoundedHeight) constraints.minHeight else height.toPx().roundToInt()
        layout(width, height) {}
    }
}