package ui.colorPicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.outsidesource.oskitcompose.color.*
import com.outsidesource.oskitcompose.form.KmpSliderDirection
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.layout.FlexRow
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import ui.common.Screen
import kotlin.math.roundToInt

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
            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(modifier = Modifier.size(100.dp).background(state.color.toColor()))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    KmpColorPickerHueSlider(
                        modifier = Modifier.width(250.dp),
                        color = state.color,
                        onChange = interactor::colorChanged,
                    )
                    Text("Hue: ${(state.color.hue * 100).roundToInt() / 100f}")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    KmpColorPickerSaturationSlider(
                        modifier = Modifier.width(250.dp),
                        color = state.color,
                        onChange = interactor::colorChanged,
                    )
                    Text("Saturation: ${(state.color.saturation * 100).roundToInt() / 100f}")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    KmpColorPickerValueSlider(
                        modifier = Modifier.width(250.dp),
                        color = state.color,
                        onChange = interactor::colorChanged,
                    )
                    Text("Value: ${(state.color.value * 100).roundToInt() / 100f}")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    KmpColorPickerAlphaSlider(
                        modifier = Modifier.width(250.dp),
                        color = state.color,
                        onChange = interactor::colorChanged,
                    )
                    Text("Alpha: ${(state.color.alpha * 100).roundToInt() / 100f}")
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(250.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                KmpColorPickerHueSlider(
                    modifier = Modifier.fillMaxHeight(),
                    color = state.color,
                    onChange = interactor::colorChanged,
                    direction = KmpSliderDirection.Vertical,
                )
                KmpColorPickerSaturationSlider(
                    modifier = Modifier.fillMaxHeight(),
                    color = state.color,
                    onChange = interactor::colorChanged,
                    direction = KmpSliderDirection.Vertical,
                )
                KmpColorPickerValueSlider(
                    modifier = Modifier.fillMaxHeight(),
                    color = state.color,
                    onChange = interactor::colorChanged,
                    direction = KmpSliderDirection.Vertical,
                )
                KmpColorPickerAlphaSlider(
                    modifier = Modifier.fillMaxHeight(),
                    color = state.color,
                    onChange = interactor::colorChanged,
                    direction = KmpSliderDirection.Vertical,
                )
            }
        }
    }
}