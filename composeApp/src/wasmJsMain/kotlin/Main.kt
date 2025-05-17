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
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.configureWebResources
import org.koin.core.component.inject
import org.w3c.dom.*
import org.w3c.dom.events.EventListener
import oskit_example_app_kmp.composeapp.generated.resources.Res
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

private external class ResizeEventDetail : JsAny {
    val width: JsNumber
    val height: JsNumber
}

private external class BlurEventDetail : JsAny {
    val direction: JsString
}

private external fun encodeURIComponent(value: String): String

private external interface CSSStyleDeclarationExt : JsAny {
    var scale: String
    var translate: String
}

@Composable
fun rememberHtmlState(): HtmlState = remember(Unit) { HtmlState() }

@OptIn(ExperimentalUuidApi::class)
@Immutable
/**
 * @param container The primary element added to the DOM. All [CustomEvent]s should be dispatched to this element.
 * @param content The content element. This is the node any custom HTML is appended to. Any custom styles
 */
data class HtmlState(
    val container: HTMLDivElement = document.createElement("div") as HTMLDivElement,
    val content: HTMLDivElement = document.createElement("div") as HTMLDivElement,
) {

    val runtimeJsUrl: String
    val runtimeJsUrlEncoded: String

    init {
        container.attachShadow(ShadowRootInit(ShadowRootMode.OPEN))
        container.id = "compose-html-${Uuid.random().toHexString()}"
        container.style.position = "absolute"
        container.style.top = "0"
        container.style.left = "0"
        container.style.zIndex = "1"
        container.style.overflowX = "hidden"
        container.style.overflowY = "hidden"

        content.id = "compose-html-${Uuid.random().toHexString()}"

        runtimeJsUrl = "${Res.getUri("files/compose-html-runtime.js")}?containerId=${container.id}&contentId=${content.id}"
        runtimeJsUrlEncoded = encodeURIComponent(runtimeJsUrl)

        val runtimeScript = document.createElement("script") as HTMLScriptElement
        runtimeScript.type = "module"
        runtimeScript.src = runtimeJsUrl
        container.shadowRoot?.appendChild(runtimeScript)
    }

    fun emit(event: CustomEvent) = container.dispatchEvent(event)
    fun addListener(type: String, listener: EventListener) = container.addEventListener(type, listener)
    fun removeListener(type: String, listener: EventListener) = container.removeEventListener(type, listener)
}

/**
 * Render arbitrary HTML within the compose layout.
 *
 * [Html] works by appending DOM nodes to the document and positioning them properly to align with the intended position
 * in the compose layout. All injected HTML is rendered in a [ShadowRoot] to avoid style/name collisions.
 *
 * Events:
 *   Use [HtmlState.emit] to dispatch custom events from Kotlin to your injected JavaScript.
 *   Use [HtmlState.addListener] to listen to custom events from JavaScript in Kotlin.
 *
 * JavaScript:
 *   [Html] supports both inline scripts and ES6 module scripts. All inline JS is injected as an ES6 module in order
 *   to isolate definitions. All scripts may import [HtmlState.runtimeJsUrl] to gain access to the `Env` object which
 *   provides access to the `container`, `content` elements as well as providing a mechanism to send and receive
 *   [CustomEvent]s to Kotlin.
 *
 *   inlineJs example:
 *   ```js
 *   import { Env } from "${htmlState.runtimeJsUrl()}"
 *
 *   function foo() {
 *      Env.emit(new CustomEvent("bar"))
 *   }
 *
 *   const button = Env.content.querySelector("button")
 *   ```
 *
 *   ES6 module example:
 *   ```js
 *   // Source Url: http://example.com/es6.js?runtimeJsUrl=${htmlState.runtimeJsUrlEncoded}
 *   const { Env } = await import(new URL(import.meta.url).searchParams.get("runtimeJsUrl"))
 *
 *   console.log(Env.container)
 *   ```
 *
 * File access:
 *   Use `Res.getUri()` to inject files/images into your HTML source
 *
 * Limitations:
 *   1. Alpha and scale graphic transformations to this composable or parent composable cannot be automatically
 *      applied to the HTML. This includes transformations that occur as a result of animations. In order to apply
 *      alpha and scale transformations, they must be applied directly to the HTML DOM elements via [HtmlState].
 *   2. Accessibility will not flow naturally as the DOM elements are outside the canvas.
 *   3. Using `iframe` will prevent scrolling while hovering over the iframe. This is due to iframes not bubbling their
 *      events up to any parent documents.
 *
 * @param state The state for the Html. This provides access to the created DOM nodes used and provides some helper
 *   functions for dispatching and listening to events.
 * @param inlineJs returns a JavaScript string to be injected into the HTML. This function only runs once unless the
 *   [HtmlState] changes.
 * @param scripts An array of JavaScript module URLs to load.
 * @param html returns the HTML string to be injected. This function only runs once unless the [HtmlState] changes.
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
fun Html(
    state: HtmlState = rememberHtmlState(),
    modifier: Modifier = Modifier,
    inlineJs: (() -> String)? = null,
    scripts: List<String> = emptyList(),
    html: () -> String,
) {
    // TODO: Demos Google Maps, Youtube embed, charts, react app?, video, audio, iframe
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current
    var htmlWidth by remember(Unit) { mutableStateOf(0.dp) }
    var htmlHeight by remember(Unit) { mutableStateOf(0.dp) }
    val constraintsRef = remember(Unit) { VarRef(Constraints(0, 0)) }

    DisposableEffect(state) {
        val shadowRoot = state.container.shadowRoot ?: return@DisposableEffect onDispose {  }
        state.content.innerHTML = html()
        shadowRoot.appendChild(state.content)

        state.container.addEventListener("compose-html-resize") {
            val data = (it as? CustomEvent)?.detail?.unsafeCast<ResizeEventDetail>() ?: return@addEventListener
            htmlWidth = data.width.toInt().dp
            htmlHeight = data.height.toInt().dp
        }
        state.container.addEventListener("compose-html-blur") {
            val data = (it as? CustomEvent)?.detail?.unsafeCast<BlurEventDetail>() ?: return@addEventListener
            val direction = if (data.direction == "next".toJsString()) FocusDirection.Down else FocusDirection.Up
            focusManager.moveFocus(direction)
        }

        if (inlineJs != null) {
            val script: HTMLScriptElement = document.createElement("script") as HTMLScriptElement
            script.type = "module"
            script.textContent = inlineJs().trimIndent()
            shadowRoot.appendChild(script)
        }

        for (scriptUrl in scripts) {
            val script: HTMLScriptElement = document.createElement("script") as HTMLScriptElement
            script.type = "module"
            script.src = scriptUrl
            shadowRoot.appendChild(script)
        }

        document.body?.appendChild(state.container)

        onDispose { document.body?.removeChild(state.container) }
    }

    Layout(
        modifier = modifier
            .onFocusChanged {
                if (!it.isFocused) return@onFocusChanged
                state.container.dispatchEvent(CustomEvent("compose-html-focus"))
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
                state.content.style.unsafeCast<CSSStyleDeclarationExt>().translate = "${left}px ${top}px"
            }
    ) { _, constraints ->
        constraintsRef.value = constraints
        val width = if (constraints.hasFixedWidth) constraints.minWidth else htmlWidth.toPx().roundToInt()
        val height = if (constraints.hasFixedHeight) constraints.minHeight else htmlHeight.toPx().roundToInt()
        layout(width, height) {}
    }
}