package ui.colorPicker

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.rotate
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.color.HsvColor
import com.outsidesource.oskitcompose.color.IKmpColorPickerRenderer
import com.outsidesource.oskitcompose.color.KmpColorPicker
import com.outsidesource.oskitcompose.color.KmpColorPickerHandle
import com.outsidesource.oskitcompose.color.KmpColorPickerRenderer
import com.outsidesource.oskitcompose.color.KmpColorPickerRendererOptions
import com.outsidesource.oskitcompose.color.toDegree
import com.outsidesource.oskitcompose.color.toRadians
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.layout.FlexRow
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import ui.common.Screen
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun ColorPickerScreen(
    interactor: ColorPickerViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    Screen(
        title = "Color Picker",
    ) {
        val colorFunc: (HsvColor, Any, Any) -> Unit = { c, _, _ -> interactor.colorChanged(c) }
        val options = remember {
            KmpColorPickerRendererOptions(
                renderAlpha = true,
                renderAlphaChecker = true,
                render3rdComponent = true,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FlexRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Start),
            ) {
                Column(modifier = Modifier.widthIn(max = 200.dp).weight(1f)) {
                    Text("Hue and Saturation", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    KmpColorPicker(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        color = state.color,
                        renderer = remember { KmpColorPickerRenderer.Hs() },
                        rendererOptions = options,
                        onChange = colorFunc,
                    )
                }
                Column(modifier = Modifier.widthIn(max = 200.dp).weight(1f)) {
                    Text("Hue and Value", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    KmpColorPicker(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        color = state.color,
                        renderer = remember { KmpColorPickerRenderer.Hv() },
                        rendererOptions = options,
                        onChange = colorFunc,
                    )
                }
                Column(modifier = Modifier.widthIn(max = 200.dp).weight(1f)) {
                    Text("Saturation and Value", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    KmpColorPicker(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        color = state.color,
                        renderer = remember { KmpColorPickerRenderer.Sv() },
                        rendererOptions = options,
                        onChange = colorFunc,
                    )
                }
            }
            FlexRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.widthIn(max = 200.dp).weight(1f)) {
                    Text("Hue and Saturation (circular)", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    KmpColorPicker(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        color = state.color,
                        renderer = remember { KmpColorPickerRenderer.HsCircle() },
                        rendererOptions = options,
                        onChange = colorFunc,
                    )
                }
                Column(modifier = Modifier.widthIn(max = 200.dp).weight(1f)) {
                    Text("Hue and Value (circular)", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    KmpColorPicker(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        color = state.color,
                        renderer = remember { KmpColorPickerRenderer.HvCircle() },
                        rendererOptions = options,
                        onChange = colorFunc,
                    )
                }
                Column(modifier = Modifier.widthIn(max = 200.dp).weight(1f)) {
                    Text("Multiple Colors", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    KmpColorPicker(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        colors = state.colors,
                        renderer = remember { KmpColorPickerRenderer.HsCircle() },
                        rendererOptions = options,
                        onChange = { key, color, _, _ -> interactor.multiColorChanged(key, color) },
                    )
                }
            }
            Column {
                Box(modifier = Modifier.size(100.dp).background(state.color.toColor()))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Slider(
                        modifier = Modifier.width(200.dp),
                        value = state.color.hue,
                        valueRange = 0f..360f,
                        onValueChange = { interactor.colorChanged(state.color.copy(hue = it)) },
                    )
                    Text("Hue: ${state.color.hue}")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Slider(
                        modifier = Modifier.width(200.dp),
                        value = state.color.saturation * 100f,
                        valueRange = 0f..100f,
                        onValueChange = { interactor.colorChanged(state.color.copy(saturation = it / 100f)) },
                    )
                    Text("Saturation: ${state.color.saturation}")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Slider(
                        modifier = Modifier.width(200.dp),
                        value = state.color.value * 100f,
                        valueRange = 0f..100f,
                        onValueChange = { interactor.colorChanged(state.color.copy(value = it / 100f)) },
                    )
                    Text("Value: ${state.color.value}")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Slider(
                        modifier = Modifier.width(200.dp),
                        value = state.color.alpha * 100f,
                        valueRange = 0f..100f,
                        onValueChange = { interactor.colorChanged(state.color.copy(alpha = it / 100f)) },
                    )
                    Text("Alpha: ${state.color.alpha}")
                }
            }
        }
    }
}