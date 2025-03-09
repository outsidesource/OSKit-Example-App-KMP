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
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.layout.FlexRow
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.modifier.outerShadow
import ui.common.Screen
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun XyzColorWheelCanvas(modifier: Modifier = Modifier) {
    // The Canvas modifier size is in dp; the drawing coordinates are in pixels.
    Canvas(modifier = modifier) {
        val canvasWidth = size.width.toInt()
        val canvasHeight = size.height.toInt()
        // Assume a square canvas so the radius is half the width.
        val radius = canvasWidth / 2f

        // D65 white chromaticity coordinates and maximum chromaticity offset.
        val whiteX = 0.3127f
        val whiteY = 0.3290f
        val rMax = 0.2f

        // Iterate over every pixel on the canvas.
        for (y in 0 until canvasHeight) {
            for (x in 0 until canvasWidth) {
                // Offset from the center.
                val dx = x - radius
                val dy = y - radius
                val distance = sqrt(dx * dx + dy * dy)
                if (distance <= radius) {
                    // Normalize distance: 0 at center (white) to 1 at the edge (full chromaticity offset).
                    val normDist = distance / radius
                    // Angle in radians.
                    val angle = atan2(dy, dx)
                    // Compute the chromaticity offset along the direction of the angle.
                    val deltaX = rMax * normDist * cos(angle)
                    val deltaY = rMax * normDist * sin(angle)
                    // New chromaticity coordinates.
                    val chromaX = whiteX + deltaX
                    val chromaY = whiteY + deltaY

                    // Use a fixed luminance Y = 1.0.
                    val Y_val = 1.0f
                    // Reconstruct X and Z from chromaticity.
                    val X_val = (chromaX / chromaY) * Y_val
                    val Z_val = ((1 - chromaX - chromaY) / chromaY) * Y_val

                    // Convert from XYZ to linear sRGB using the standard conversion matrix.
                    val rLinear =  3.2406f * X_val - 1.5372f * Y_val - 0.4986f * Z_val
                    val gLinear = -0.9689f * X_val + 1.8758f * Y_val + 0.0415f * Z_val
                    val bLinear =  0.0557f * X_val - 0.2040f * Y_val + 1.0570f * Z_val

                    // Gamma correction (sRGB).
                    fun linearToSrgb(c: Float): Float =
                        if (c <= 0.0031308f) 12.92f * c
                        else (1.055f * c.toDouble().pow(1.0 / 2.4)).toFloat() - 0.055f

                    val r = linearToSrgb(rLinear).coerceIn(0f, 1f)
                    val g = linearToSrgb(gLinear).coerceIn(0f, 1f)
                    val b = linearToSrgb(bLinear).coerceIn(0f, 1f)

                    // Create a Compose Color from the computed sRGB values.
                    val color = Color(red = r, green = g, blue = b, alpha = 1f)

                    // Draw a 1×1 rectangle at (x, y) with the computed color.
                    drawRect(
                        color = color,
                        topLeft = Offset(x.toFloat(), y.toFloat()),
                        size = Size(1f, 1f)
                    )
                }
                // Outside the circle: do not draw anything (the canvas background remains transparent).
            }
        }
    }
}

// Composable that draws an LCH color wheel
@Composable
fun LchColorWheel(modifier: Modifier = Modifier.size(300.dp)) {
    Canvas(modifier = modifier) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        // Use the smaller half-dimension as the wheel's radius
        val radius = min(centerX, centerY)

        // Loop over radial steps and angular degrees.
        // For performance, we iterate over discrete steps.
        for (r in 0 until radius.toInt()) {
            val rad = r.toFloat()
            for (angle in 0 until 360) {
                val angleRad = toRadians(angle.toDouble())

                // Set a fixed lightness and map the radius to chroma.
                // Adjust these values as needed.
                val l = 70.0
                val maxChroma = 80.0
                val c = (rad / radius) * maxChroma
                val h = angle.toDouble() // Hue in degrees

                // Convert LCH to a Color
                val color = lchToColor(l, c, h)

                // Compute the (x, y) coordinate for this point.
                val x = centerX + rad * cos(angleRad).toFloat()
                val y = centerY + rad * sin(angleRad).toFloat()

                // Draw a small circle (a "pixel") at (x, y)
                drawCircle(color = color, radius = 1.5.dp.toPx(), center = Offset(x, y))
            }
        }
    }
}

fun toRadians(degrees: Double): Double {
    return degrees * (PI / 180.0)
}

// Function to convert LCH (L, chroma, hue) to an sRGB Color.
// The conversion goes LCH -> Lab -> XYZ -> sRGB.
fun lchToColor(l: Double, c: Double, h: Double): Color {
    // Convert LCH to Lab
    val a = c * cos(toRadians(h))
    val b = c * sin(toRadians(h))

    // Convert Lab to XYZ using D65 reference white
    val refX = 95.047
    val refY = 100.0
    val refZ = 108.883

    val yCalc = (l + 16.0) / 116.0
    val xCalc = yCalc + (a / 500.0)
    val zCalc = yCalc - (b / 200.0)

    val xCalc3 = xCalc * xCalc * xCalc
    val yCalc3 = yCalc * yCalc * yCalc
    val zCalc3 = zCalc * zCalc * zCalc
    val epsilon = 0.008856
    val kappa = 903.3

    val X = refX * if (xCalc3 > epsilon) xCalc3 else (xCalc - 16.0/116.0) / 7.787
    val Y = refY * if (l > kappa * epsilon) yCalc3 else l / kappa
    val Z = refZ * if (zCalc3 > epsilon) zCalc3 else (zCalc - 16.0/116.0) / 7.787

    // Convert XYZ to linear sRGB
    // Scale XYZ to the [0, 1] range
    var rLin =  3.2406 * (X / 100) - 1.5372 * (Y / 100) - 0.4986 * (Z / 100)
    var gLin = -0.9689 * (X / 100) + 1.8758 * (Y / 100) + 0.0415 * (Z / 100)
    var bLin =  0.0557 * (X / 100) - 0.2040 * (Y / 100) + 1.0570 * (Z / 100)

    // Apply gamma correction to convert to sRGB
    val r = if (rLin > 0.0031308) 1.055 * rLin.pow(1.0/2.4) - 0.055 else 12.92 * rLin
    val g = if (gLin > 0.0031308) 1.055 * gLin.pow(1.0/2.4) - 0.055 else 12.92 * gLin
    val blue = if (bLin > 0.0031308) 1.055 * bLin.pow(1.0/2.4) - 0.055 else 12.92 * bLin

    // Clamp each channel to the [0, 1] range and return the Color
    return Color(
        red = r.coerceIn(0.0, 1.0).toFloat(),
        green = g.coerceIn(0.0, 1.0).toFloat(),
        blue = blue.coerceIn(0.0, 1.0).toFloat(),
        alpha = 1f
    )
}

@Composable
fun TestWheel(modifier: Modifier) {
    Canvas(modifier.clip(CircleShape).blur(16.dp)) {
        drawCircle(color = Color.White)
        drawCircle(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color.Red,
                    Color.Yellow,
                    Color.Green,
                    Color.Cyan,
                    Color.Blue,
                    Color.Magenta,
                    Color.Red,
                ),
            ),
            center = size.center,
            radius = size.width / 2f,
        )

        drawCircle(
            brush = Brush.radialGradient(
                center = size.center,
                radius = size.width / 2f,
                colors = listOf(Color.White, Color.Transparent),
            ),
            center = size.center,
            radius = size.width / 2f,
        )
    }
}


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
            TestWheel(modifier = Modifier.size(300.dp))
//            XyzColorWheelCanvas(modifier = Modifier.size(300.dp))
//            LchColorWheel(modifier = Modifier.size(300.dp))
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
                    direction = ColorPickerSliderDirection.Vertical,
                )
                KmpColorPickerSaturationSlider(
                    modifier = Modifier.fillMaxHeight(),
                    color = state.color,
                    onChange = interactor::colorChanged,
                    direction = ColorPickerSliderDirection.Vertical,
                )
                KmpColorPickerValueSlider(
                    modifier = Modifier.fillMaxHeight(),
                    color = state.color,
                    onChange = interactor::colorChanged,
                    direction = ColorPickerSliderDirection.Vertical,
                )
                KmpColorPickerAlphaSlider(
                    modifier = Modifier.fillMaxHeight(),
                    color = state.color,
                    onChange = interactor::colorChanged,
                    direction = ColorPickerSliderDirection.Vertical,
                )
            }
        }
    }
}

        }
    }
}