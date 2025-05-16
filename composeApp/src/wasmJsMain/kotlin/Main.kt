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
import org.w3c.dom.events.EventListener
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
        }
    }

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
                state = htmlState,
                script = {
                    """
                        function test() {
                            console.log("Hello!")
                            OsKit.emit(new CustomEvent("hello"))
                        }
                        
                        OsKit.content.querySelector("button").addEventListener("click", test)
                        """
                },
                html = {
                    """
                        <iframe width="100%" style="max-width: 500px; aspect-ratio: 16/9;" src="https://www.youtube.com/embed/0NDqYZVbpho?si=_tjA9VpXKSHiY1hy" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
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
fun PageTwo(direction: String) {
    val htmlState = rememberHtmlState()
    val alphaAnim = remember { Animatable(0f) }
    LaunchedEffect(direction) {
        alphaAnim.animateTo(if (direction == "in") 1f else 0f, tween(1000))
    }

    LaunchedEffect(alphaAnim) {
        snapshotFlow { alphaAnim.value }.collect {
            htmlState.content.style.opacity = it.toString()
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

external class ResizeEventDetail : JsAny {
    val width: JsNumber
    val height: JsNumber
}

external class BlurEventDetail : JsAny {
    val direction: JsString
}

@Composable
fun rememberHtmlState(): HtmlState {
    return remember(Unit) {
        HtmlState(
            container = document.createElement("div") as HTMLDivElement,
            content = document.createElement("div") as HTMLDivElement,
        )
    }
}

@Immutable
data class HtmlState(
    val container: HTMLElement,
    val content: HTMLElement,
) {
    fun emit(event: CustomEvent) = container.dispatchEvent(event)
    fun listen(type: String, listener: EventListener) = container.addEventListener(type, listener)
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
    state: HtmlState = rememberHtmlState(),
    modifier: Modifier = Modifier,
    script: (() -> String)? = null,
    html: () -> String,
) {
    // TODO: Make sure these remembered scopes are ok
    // TODO: Add removing of event listener
    // TODO: Add setting of scale and opacity to state (graphics layer or something)
    // TODO: Demos Google Maps, Youtube embed, charts, react app?, video, audio, iframe
    // TODO: Move dom setup into state creator
    // TODO: The event proxy and resize observer need to be added regardless of if there is a user provided script
    // TODO: Only attach resize observer if constraints aren't defined?
    // TODO: Rename OsKit to runtime or env or something
    // TODO: Iframes aren't going to proxy their events properly
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current
    val scriptElement = remember(script) { document.createElement("script") as HTMLScriptElement }
    var htmlWidth by remember(Unit) { mutableStateOf(0.dp) }
    var htmlHeight by remember(Unit) { mutableStateOf(0.dp) }
    val constraintsRef = remember(Unit) { VarRef(Constraints(0, 0)) }

    DisposableEffect(Unit) {
        val shadowRoot = state.container.attachShadow(ShadowRootInit(ShadowRootMode.OPEN))
        state.container.className = "oskit-${Uuid.random().toHexString()}"
        state.container.style.position = "absolute"
        state.container.style.top = "0"
        state.container.style.left = "0"
        state.container.style.zIndex = "1"
        state.container.style.overflowX = "hidden"
        state.container.style.overflowY = "hidden"

        state.content.className = "oskit-${Uuid.random().toHexString()}"
        state.content.innerHTML = html()
        shadowRoot.appendChild(state.content)

        if (script != null) {
            state.container.addEventListener("oskit-resize") {
                val data = (it as? CustomEvent)?.detail?.unsafeCast<ResizeEventDetail>() ?: return@addEventListener
                htmlWidth = data.width.toInt().dp
                htmlHeight = data.height.toInt().dp
            }
            state.container.addEventListener("oskit-blur") {
                val data = (it as? CustomEvent)?.detail?.unsafeCast<BlurEventDetail>() ?: return@addEventListener
                val direction = if (data.direction == "next".toJsString()) FocusDirection.Down else FocusDirection.Up
                focusManager.moveFocus(direction)
            }
            scriptElement.type = "module"
            scriptElement.textContent = """
                const OsKit = (() => {
                    const canvas = document.querySelector("canvas")
                    const container = document.querySelector(".${state.container.className}")
                    const content = container.shadowRoot.querySelector(".${state.content.className}")

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
                        emit: (ev) => container.dispatchEvent(ev),
                        listen: (type, listener) => container.addEventListener(type, listener)
                    })
                })();
            """.trimIndent() + script().trimIndent()
            shadowRoot.appendChild(scriptElement)
        }

        document.body?.appendChild(state.container)

        onDispose { document.body?.removeChild(state.container) }
    }

    Layout(
        modifier = modifier
            .onFocusChanged {
                if (!it.isFocused) return@onFocusChanged
                state.container.dispatchEvent(CustomEvent("oskit-focus"))
            }
            .focusable()
            .onGloballyPositioned { layoutCoordinates ->
                val bounds = layoutCoordinates.boundsInRoot().let { Rect(it.topLeft / density.density, it.size / density.density) }
                val rootPos = layoutCoordinates.positionInRoot() / density.density
                val size = layoutCoordinates.size.let { Size(it.width / density.density, it.height / density.density) }

                state.container.style.height = "${bounds.height}px"
                state.container.style.width = "${bounds.width}px"
                state.container.style.transform = "translate(${bounds.left}px, ${bounds.top}px)"

                state.content.style.width = if (constraintsRef.value.hasFixedWidth) "${constraintsRef.value.minWidth}px" else "auto"
                state.content.style.minWidth = if (constraintsRef.value.hasBoundedWidth) "${constraintsRef.value.minWidth}px" else "auto"
                state.content.style.maxWidth = if (constraintsRef.value.hasBoundedWidth) "${constraintsRef.value.maxWidth}px" else "auto"
                state.content.style.height = if (constraintsRef.value.hasFixedHeight) "${constraintsRef.value.minHeight}px" else "auto"
                state.content.style.minHeight = if (constraintsRef.value.hasBoundedHeight) "${constraintsRef.value.minHeight}px" else "auto"
                state.content.style.maxHeight = if (constraintsRef.value.hasBoundedHeight) "${constraintsRef.value.maxHeight}px" else "auto"

                val top = if (bounds.height < size.height && rootPos.y < bounds.top) rootPos.y - bounds.top else 0
                val left = if (bounds.width < size.width && rootPos.x < bounds.left) rootPos.x - bounds.left else 0
                state.content.style.transform = "translate(${left}px, ${top}px)"
            }
    ) { _, constraints ->
        constraintsRef.value = constraints
        val width = if (constraints.hasFixedWidth) constraints.minWidth else htmlWidth.toPx().roundToInt()
        val height = if (constraints.hasFixedHeight) constraints.minHeight else htmlHeight.toPx().roundToInt()
        layout(width, height) {}
    }
}