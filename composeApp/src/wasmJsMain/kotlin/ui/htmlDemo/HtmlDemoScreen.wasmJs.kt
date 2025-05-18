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
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.html.Html
import com.outsidesource.oskitcompose.html.rememberHtmlState
import kotlinx.coroutines.delay
import org.w3c.dom.CustomEvent
import ui.app.RouteTransitionDirection
import ui.app.theme.AppTheme
import ui.common.Screen

@Composable
actual fun HtmlDemoScreen(transitionDirection: RouteTransitionDirection) {
    Screen(
        title = "Html Demo",
        paddingValues = PaddingValues(0.dp),
    ) {

        val videoHtmlState = rememberHtmlState()
        val youtubeHtmlState = rememberHtmlState()
        val cameraHtmlState = rememberHtmlState()
        val chartHtmlState = rememberHtmlState()
        val textHtmlState = rememberHtmlState()

        val alphaAnim = remember { Animatable(0f) }

        LaunchedEffect(transitionDirection) {
            alphaAnim.animateTo(if (transitionDirection == RouteTransitionDirection.In) 1f else 0f, tween(400))
        }

        LaunchedEffect(alphaAnim) {
            snapshotFlow { alphaAnim.value }.collect {
                videoHtmlState.content.style.opacity = it.toString()
                youtubeHtmlState.content.style.opacity = it.toString()
                cameraHtmlState.content.style.opacity = it.toString()
                chartHtmlState.content.style.opacity = it.toString()
                textHtmlState.content.style.opacity = it.toString()
            }
        }

        LaunchedEffect(Unit) {
            var count = 0
            while (true) {
                delay(500)
                count++
                textHtmlState.content.querySelector(".count")?.innerHTML = "$count"
            }
        }

        LaunchedEffect(Unit) {
            textHtmlState.listen("mouse-moved").collect {
                val event = it.detail?.unsafeCast<MouseMoveEvent>() ?: return@collect
                println("Mouse Moved: ${event.x}, ${event.y}")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.padding(AppTheme.dimensions.screenPadding),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = { videoHtmlState.emit(CustomEvent("play-video")) },
                        content = { Text("Play Video") },
                    )
                }
                Box(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = { cameraHtmlState.emit(CustomEvent("toggle-camera")) },
                        content = { Text("Toggle Camera") },
                    )
                }
            }
            Row(
                modifier = Modifier.padding(AppTheme.dimensions.screenPadding),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Video Demo
                Html(
                    modifier = Modifier.weight(1f),
                    state = videoHtmlState,
                    inlineJs = {
                        """
                            import { Env } from "${videoHtmlState.runtimeJsUrl}"
                            Env.addListener("play-video", () => Env.content.querySelector("video").play())
                        """
                    }
                ) {
                    """
                    <video id='video' controls="controls" preload='none' width="100%" style="aspect-ratio: 16/9;">
                        <source id='mp4' src="http://media.w3.org/2010/05/sintel/trailer.mp4" type='video/mp4' />
                        <source id='webm' src="http://media.w3.org/2010/05/sintel/trailer.webm" type='video/webm' />
                        <source id='ogv' src="http://media.w3.org/2010/05/sintel/trailer.ogv" type='video/ogg' />
                    </video> 
                    """
                }

                // Youtube Demo
                Html(
                    modifier = Modifier.weight(1f),
                    state = youtubeHtmlState,
                ) {
                    """                    
                    <iframe 
                        width="100%" 
                        style="aspect-ratio: 16/9;" 
                        src="https://www.youtube.com/embed/0NDqYZVbpho?si=_tjA9VpXKSHiY1hy" 
                        title="YouTube Example" 
                        frameborder="0" 
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
                        referrerpolicy="strict-origin-when-cross-origin" 
                        allowfullscreen
                    >
                    </iframe>
                    """
                }

                // Camera Preview
                Html(
                    modifier = Modifier.weight(1f),
                    state = cameraHtmlState,
                    inlineJs = {
                        """
                        import { Env } from "${cameraHtmlState.runtimeJsUrl}"
                        
                        Env.addListener("toggle-camera", () => {
                            const videoElement = Env.content.querySelector("#camera-preview")
                            if (!videoElement.srcObject) {
                                startCamera()
                            } else {
                                stopCamera()
                            }
                        })
                            
                        async function startCamera() {
                            try {
                                const stream = await navigator.mediaDevices.getUserMedia({ video: true })
                                const videoElement = Env.content.querySelector("#camera-preview")
                                videoElement.srcObject = stream
                            } catch (error) {
                                console.error("Error accessing camera:", error)
                            }
                        }
                        
                        function stopCamera() {
                            const videoElement = Env.content.querySelector("#camera-preview")
                            const stream = videoElement.srcObject
                        
                            if (stream) {
                                const tracks = stream.getTracks()
                                tracks.forEach(track => track.stop())
                                videoElement.srcObject = null
                            }
                        }
                        """
                    },
                    html = {
                        """
                        <video 
                            id="camera-preview" 
                            autoplay 
                            playsinline 
                            style="width: 100%; aspect-ratio: 16/9; background: black;"
                        >
                        </video>
                        """
                    }
                )
            }
            Html(
                modifier = Modifier
                    .padding(AppTheme.dimensions.screenPadding)
                    .widthIn(max = 800.dp)
                    .aspectRatio(16f/9f)
                    .fillMaxWidth(),
                state = chartHtmlState,
                scripts = listOf("https://cdn.jsdelivr.net/npm/chart.js"),
                inlineJs = {
                    """
                        import { Env } from "${chartHtmlState.runtimeJsUrl}"
                        import "https://cdn.jsdelivr.net/npm/chart.js"
                        
                        const ctx = Env.content.querySelector("#chart")
                        
                        new Chart(ctx, {
                            type: 'bar',
                            data: {
                            labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
                            datasets: [{
                                label: '# of Votes',
                                data: [12, 19, 3, 5, 2, 3],
                                borderWidth: 1
                            }]
                        },
                        options: {
                            scales: {
                                y: {
                                    beginAtZero: true
                                }
                            }
                        }
                      })
                    """
                },
                html = { """<canvas id="chart"></canvas>"""  }
            )
            Html(
                modifier = Modifier
                    .padding(AppTheme.dimensions.screenPadding)
                    .fillMaxWidth(),
                state = textHtmlState,
                inlineJs = {
                    """
                    import { Env } from "${textHtmlState.runtimeJsUrl}"

                    Env.container.addEventListener("mousemove", (ev) => {
                        Env.emit(new CustomEvent("mouse-moved", { detail: { x: ev.pageX, y: ev.pageY } }))
                    })
                    """
                },
                html = {
                    """
                    <style>
                        :host { 
                            font-size: 18px;
                            line-height: 1.5;
                        }
                        .demo-cont {
                            display: grid;
                            grid-template-columns: 1fr;
                            gap: 20px;
                        }
                    </style>
                    <div class="demo-cont">
                        <div class="demo-section">
                            <div><strong>Accessibility and tabbing is handled:</strong></div>
                            <input type="text" />
                        </div>
                        <div class="demo-section">
                            <div><strong>This count is being updated from a coroutine:</strong></div>
                            <div class="count">0</div></div>
                        </div>
                        <div class="demo-section">
                            <p>
                                <strong>Resizing the window will reflow the content and change the composable's size and layout appropriately. Scrolling is also handled properly:</strong> 
                            </p>
                            <p>
                                Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.
                            </p>
                            <p>
                                Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.
                            </p>
                            <p>
                                Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.
                            </p>
                            <p>
                                Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.
                            </p>
                            <p>
                                Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.
                            </p>
                        </div>
                    </div>
                    """
                },
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .height(60.dp)
                    .background(AppTheme.colors.primary)
            )
        }
    }
}

private external interface MouseMoveEvent : JsAny {
    val x: Int
    val y: Int
}