package ui.htmlDemo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.html.Html
import com.outsidesource.oskitcompose.html.rememberHtmlState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.app.RouteTransitionDirection
import ui.app.theme.AppTheme
import ui.common.Screen

// TODO: Demos Google Maps, Youtube embed, charts, react app?, video, audio, iframe
@Composable
actual fun HtmlDemoScreen(transitionDirection: RouteTransitionDirection) {
    Screen(
        title = "Html Demo",
        paddingValues = PaddingValues(0.dp),
    ) {
        val htmlState = rememberHtmlState()
        val alphaAnim = remember { Animatable(0f) }
        LaunchedEffect(transitionDirection) {
            alphaAnim.animateTo(if (transitionDirection == RouteTransitionDirection.In) 1f else 0f, tween(400))
        }

        LaunchedEffect(alphaAnim) {
            snapshotFlow { alphaAnim.value }.collect {
                htmlState.content.style.opacity = it.toString()
            }
        }

        LaunchedEffect(Unit) {
            var count = 0
            while (true) {
                delay(500)
                count++
                htmlState.content.querySelector(".count")?.innerHTML = "$count"
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(AppTheme.dimensions.screenPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = {},
                    content = { Text("Start Animation") },
                )
                Button(
                    onClick = {},
                    content = { Text("Start Camera") },
                )
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
                    <style>
                        :host {  }
                    </style>
                    <iframe width="100%" style="max-width: 500px; aspect-ratio: 16/9;" src="https://www.youtube.com/embed/0NDqYZVbpho?si=_tjA9VpXKSHiY1hy" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
                    <div style="width: 100%; height: 100%;">
                        <div>Hello <button>Click</button> <div class="count">0</div></div>
                        <input type="text" />
                    </div>
                    """
                },
            )
        }
    }
}