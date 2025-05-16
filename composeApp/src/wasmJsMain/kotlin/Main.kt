import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeViewport
import com.outsidesource.oskitcompose.lib.VarRef
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.KmpCapabilityContext
import com.outsidesource.oskitkmp.filesystem.KmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import kotlinx.browser.document
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.configureWebResources
import org.koin.core.component.inject
import org.w3c.dom.*
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

@OptIn(ExperimentalResourceApi::class)
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
//                TextField(value = "", onValueChange = {})
                Button(onClick = {}) { Text("Test")}
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
            ) {
//                TextField(value = "", onValueChange = {})
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

external class ResizeEventDetail : JsAny {
    val width: JsNumber
    val height: JsNumber
}

external class BlurEventDetail : JsAny {
    val direction: JsString
}

/**
 * Render HTML inline
 *
 * File access: Use `Res.getUri()` to inject files/images into your HTML source
 *
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
    // TODO: Make a separate rememberHtmlState() to remember the nodes
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current
    val container = remember(Unit) { document.createElement("div") as HTMLDivElement }
    val content = remember(Unit) { document.createElement("div") as HTMLDivElement }
    val scriptElement = remember(script) { document.createElement("script") as HTMLScriptElement }
    var htmlWidth by remember(Unit) { mutableStateOf(0.dp) }
    var htmlHeight by remember(Unit) { mutableStateOf(0.dp) }
    val constraintsRef = remember(Unit) { VarRef(Constraints(0, 0)) }

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
                val data = (it as? CustomEvent)?.detail?.unsafeCast<ResizeEventDetail>() ?: return@addEventListener
                htmlWidth = data.width.toInt().dp
                htmlHeight = data.height.toInt().dp
            }
            container.addEventListener("oskit-blur") {
                val data = (it as? CustomEvent)?.detail?.unsafeCast<BlurEventDetail>() ?: return@addEventListener
                val direction = if (data.direction == "next".toJsString()) FocusDirection.Down else FocusDirection.Up
                focusManager.moveFocus(direction)
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
                    const mouseEvents = ["click", "dblclick", "mousedown", "mouseenter", "mouseleave", "mouseout", "mouseover", "mouseup"]
                    mouseEvents.forEach((type) => container.addEventListener(type, (ev) => canvas.dispatchEvent(new MouseEvent(type, ev))))

                    const keyboardEvents = ["keyup", "keypress"]
                    keyboardEvents.forEach((type) => container.addEventListener(type, (ev) => canvas.dispatchEvent(new KeyboardEvent(type, ev))))
                    container.addEventListener("keydown", (ev) => {                        
                        if (ev.key !== "Tab") {
                             canvas.dispatchEvent(new KeyboardEvent("keydown", ev))
                             return
                        }

                        const focusables = getFocusableElements(content)
                        if (focusables.length > 0) {
                            if (ev.shiftKey) {
                                if (container.shadowRoot.activeElement === focusables[0]) {
                                    container.dispatchEvent(new CustomEvent("oskit-blur", {detail: {direction: "previous"}}))
                                }
                            } else if (container.shadowRoot.activeElement === focusables[focusables.length - 1]) {
                                container.shadowRoot.activeElement.blur()
                                container.dispatchEvent(new CustomEvent("oskit-blur", {detail: {direction: "next"}}))
                                ev.preventDefault()
                            }
                            return
                        }
                    })
                    
                    function getFocusableElements(root) {
                        const selector = [
                            "a[href]",
                            "area[href]",
                            "input:not([disabled])",
                            "select:not([disabled])",
                            "textarea:not([disabled])",
                            "button:not([disabled])",
                            "iframe",
                            "object",
                            "embed",
                            "[contenteditable]",
                            "[tabindex]"
                        ].join(",")
                      
                        return Array.from(root.querySelectorAll(selector)).filter(el => el.tabIndex >= 0);
                    }
                    
                    container.addEventListener("oskit-focus", () => {
                        const focusableElements = getFocusableElements(content)
                        if (focusableElements.length === 0) return
                        focusableElements[0].focus()
                    })
                    
                    const observer = new ResizeObserver((ev) => {
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
        modifier = modifier
            .onFocusChanged {
                if (!it.isFocused) return@onFocusChanged
                container.dispatchEvent(CustomEvent("oskit-focus"))
            }
            .focusable()
            .onGloballyPositioned { layoutCoordinates ->
                val bounds = layoutCoordinates.boundsInRoot().let { Rect(it.topLeft / density.density, it.size / density.density) }
                val rootPos = layoutCoordinates.positionInRoot() / density.density
                val size = layoutCoordinates.size.let { Size(it.width / density.density, it.height / density.density) }

                container.style.height = "${bounds.height}px"
                container.style.width = "${bounds.width}px"
                container.style.transform = "translate(${bounds.left}px, ${bounds.top}px)"

                content.style.width = if (constraintsRef.value.hasFixedWidth) "${constraintsRef.value.minWidth}px" else "auto"
                content.style.minWidth = if (constraintsRef.value.hasBoundedWidth) "${constraintsRef.value.minWidth}px" else "auto"
                content.style.maxWidth = if (constraintsRef.value.hasBoundedWidth) "${constraintsRef.value.maxWidth}px" else "auto"
                content.style.height = if (constraintsRef.value.hasFixedHeight) "${constraintsRef.value.minHeight}px" else "auto"
                content.style.minHeight = if (constraintsRef.value.hasBoundedHeight) "${constraintsRef.value.minHeight}px" else "auto"
                content.style.maxHeight = if (constraintsRef.value.hasBoundedHeight) "${constraintsRef.value.maxHeight}px" else "auto"

                val top = if (bounds.height < size.height && rootPos.y < bounds.top) rootPos.y - bounds.top else 0
                val left = if (bounds.width < size.width && rootPos.x < bounds.left) rootPos.x - bounds.left else 0
                content.style.transform = "translate(${left}px, ${top}px)"
            }
    ) { _, constraints ->
        constraintsRef.value = constraints
        val width = if (constraints.hasFixedWidth) constraints.minWidth else htmlWidth.toPx().roundToInt()
        val height = if (constraints.hasFixedHeight) constraints.minHeight else htmlHeight.toPx().roundToInt()
        layout(width, height) {}
    }
}