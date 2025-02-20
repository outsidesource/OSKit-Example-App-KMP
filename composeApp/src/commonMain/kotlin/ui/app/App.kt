package ui.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.rotate
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInject
import com.outsidesource.oskitcompose.systemui.SystemBarColorEffect
import com.outsidesource.oskitcompose.systemui.SystemBarIconColor
import org.koin.core.parameter.parametersOf
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun App(
    deepLink: String? = null,
    interactor: AppViewInteractor = rememberInject<AppViewInteractor> { parametersOf(deepLink) }
) {
    val state = interactor.collectAsState()

    SystemBarColorEffect(
        statusBarIconColor = SystemBarIconColor.Light,
    )

//    AppTheme(
//        colorsOverride = if (state.isDarkTheme) DarkAppColors else LightAppColors
//    ) {
//        RouteSwitch(interactor.coordinator) {
//            when (it) {
//                is Route.Home -> HomeScreen()
//                is Route.AppStateExample -> AppStateExampleScreen()
//                is Route.ViewStateExample -> ViewStateExampleScreen(it.depth)
//                is Route.DeviceHome -> DeviceHomeScreen(it.deviceId)
//                is Route.FileHandling -> FileSystemScreen()
//                is Route.Resources -> ResourcesScreen()
//                is Route.Markdown -> MarkdownScreen()
//                is Route.Popups -> PopupsScreen()
//                is Route.IOSServices -> IOSServicesScreen()
//                is Route.Widgets -> WidgetsScreen()
//                is Route.Capability -> CapabilityScreen()
//            }
//        }
//    }

    // TODO: Fix pointerInput issues with getting the current color

    var color by remember { mutableStateOf(Color.Black) }
    var calculatedColor by remember { mutableStateOf<KmpColor>(HsvColor(0f, 0f, 0f)) }

    val colorFunc: (Color, KmpColor) -> Unit = { imageColor, calcColor ->
        color = imageColor
        calculatedColor = calcColor
        println("Bitmap RGB: ${255 * color.red},${255 * color.green},${255 * color.blue}")
        println("Calculated: ${255 * calculatedColor.red},${255 * calculatedColor.green},${255 * calculatedColor.blue}")
        println("Calculated HSV: ${calculatedColor.hue},${calculatedColor.saturation},${calculatedColor.value}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
    ) {
        Row {
            ColorPicker(calculatedColor, HvDrawer, colorFunc)
            ColorPicker(calculatedColor, HsDrawer, colorFunc)
            ColorPicker(calculatedColor, SvDrawer, colorFunc)
            ColorPicker(calculatedColor, HsCircleDrawer, colorFunc)
            ColorPicker(calculatedColor, HvCircleDrawer, colorFunc)
            Column {
                Box(modifier = Modifier.size(100.dp).background(color))
                Box(modifier = Modifier.size(100.dp).background(calculatedColor.toColor()))
            }
        }
        Column(
            modifier = Modifier.width(200.dp)
        ) {
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = calculatedColor.hue,
                valueRange = 0f..360f,
                onValueChange = { calculatedColor = HsvColor(it, calculatedColor.saturation, calculatedColor.value) },
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = calculatedColor.saturation * 100f,
                valueRange = 0f..100f,
                onValueChange = { calculatedColor = HsvColor(calculatedColor.hue, it / 100f, calculatedColor.value) },
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = calculatedColor.value * 100f,
                valueRange = 0f..100f,
                onValueChange = {
                    calculatedColor = HsvColor(calculatedColor.hue, calculatedColor.saturation, it / 100f)
                },
            )
        }
    }
}

sealed class KmpColor {
    abstract val hue: Float // 0-360
    abstract val saturation: Float // 0-1
    abstract val value: Float // 0-1
    abstract val red: Float // 0-1
    abstract val blue: Float // 0-1
    abstract val green: Float // 0-1
//    abstract fun luminance: Float

//    abstract fun saturate(value: Float): KmpColor
//    abstract fun desaturate(value: Float): KmpColor
//    abstract fun lighten(value: Float): KmpColor
//    abstract fun darken(value: Float): KmpColor
//    abstract fun shiftHue(value: Int): KmpColor

    abstract fun toColor(): Color
}

//data class RgbColor(val r: Int, val g: Int, val b: Int, val a: Int): KmpColor()

data class HsvColor(
    override val hue: Float,
    override val saturation: Float,
    override val value: Float
): KmpColor() {
    val rgbValue: Color by lazy { hsvToRgb(hue.toFloat(), saturation, value) }

    override val red: Float = rgbValue.red
    override val blue: Float = rgbValue.blue
    override val green: Float = rgbValue.green


    override fun toColor(): Color = hsvToRgb(hue.toFloat(), saturation, value)
}



@Composable
fun ColorPicker(
    color: KmpColor,
    drawer: IKmpColorPickerDrawer,
    onChange: (Color, KmpColor) -> Unit,
) {
    val density = LocalDensity.current
    val bitmap = remember(color) {
        val bitmap = with(density) { ImageBitmap(200.dp.toPx().toInt(), 200.dp.toPx().toInt()) }
        val canvas = Canvas(bitmap)
        drawer.drawFunc(color, canvas, Size(bitmap.width.toFloat(), bitmap.height.toFloat()))
        bitmap
    }

    Canvas(
        modifier = Modifier
            .size(200.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    val pixel = bitmap.toPixelMap(
                        startX = it.x.toInt(),
                        startY = it.y.toInt(),
                        width = 1,
                        height = 1,
                    )
                    onChange(Color(pixel.buffer[0]), drawer.colorForOffset(color, it, size))
                })
            }
    ) {
        drawIntoCanvas { drawer.drawFunc(color, it, size) }
    }
}

fun Double.toDegree(): Double = this * 180.0 / PI.toDouble()

//fun hsvToRgb(h: Float, s: Float, v: Float): Color {
//    if (h.isNaN() || s.isNaN() || s < 1e-7) return Color(v, v, v)
//    val v = v.toDouble()
//    val h = (h.coerceIn(0f, 360f) / 60.0)
//    val s = s.toDouble()
//
//    fun f(n: Int): Float {
//        val k = (n + h) % 6
//        return (v - v * s * minOf(k, 4 - k, 1.0).coerceAtLeast(0.0)).toFloat()
//    }
//
//    return Color(f(5), f(3), f(1), 1f)
//}

fun hsvToRgb(h: Float, s: Float, v: Float): Color {
    if (s == 0f) {
        val gray = (v * 255).roundToInt().coerceIn(0, 255)
        return Color(gray, gray, gray)
    }

    val hPrime = h / 60f
    val sector = hPrime.toInt()
    val fraction = hPrime - sector

    val p = v * (1 - s)
    val q = v * (1 - s * fraction)
    val t = v * (1 - s * (1 - fraction))

    val (rf, gf, bf) = when (sector % 6) {
        0 -> Triple(v, t, p)
        1 -> Triple(q, v, p)
        2 -> Triple(p, v, t)
        3 -> Triple(p, q, v)
        4 -> Triple(t, p, v)
        5 -> Triple(v, p, q)
        else -> Triple(0f, 0f, 0f)
    }

    val r = (rf * 255).roundToInt().coerceIn(0, 255)
    val g = (gf * 255).roundToInt().coerceIn(0, 255)
    val b = (bf * 255).roundToInt().coerceIn(0, 255)

    return Color(r, g, b)
}

interface IKmpColorPickerDrawer {
    fun drawFunc(color: KmpColor, canvas: Canvas, size: Size)
    fun colorForOffset(color: KmpColor, offset: Offset, size: IntSize): KmpColor
    fun offsetForColor(color: Color, size: IntSize): Offset
}

val SvDrawer = object : IKmpColorPickerDrawer {
    override fun drawFunc(color: KmpColor, canvas: Canvas, size: Size) {
        val fullHueColor = Color.hsv(color.hue, 1f, 1f)
        canvas.drawRect(
            paint = Paint().apply { this.color = Color.White },
            rect = Rect(Offset(0f, 0f), size),
        )

        canvas.drawRect(
            paint = Paint().apply {
                shader = LinearGradientShader(
                    from = Offset(0f, 0f),
                    to = Offset(size.width, 0f),
                    colors = listOf(Color.White, fullHueColor),
                )
            },
            rect = Rect(Offset(0f, 0f), size),
        )

        canvas.drawRect(
            paint = Paint().apply {
                blendMode = BlendMode.Multiply
                shader = LinearGradientShader(
                    from = Offset(0f, 0f),
                    to = Offset(0f, size.height),
                    colors = listOf(Color.Transparent, Color.Black),
                )
            },
            rect = Rect(Offset(0f, 0f), size),
        )
    }

    override fun colorForOffset(
        color: KmpColor,
        offset: Offset,
        size: IntSize
    ): KmpColor {
        val saturation = ((100f / size.width) * offset.x).coerceIn(0f..100f) / 100f
        val value = (100f - ((100f / size.height) * offset.y).coerceIn(0f..100f)) / 100f
        return HsvColor(color.hue, saturation, value)
    }

    override fun offsetForColor(
        color: Color,
        size: IntSize
    ): Offset {
        TODO("Not yet implemented")
    }

}

val HvDrawer = object : IKmpColorPickerDrawer {
    override fun drawFunc(color: KmpColor, canvas: Canvas, size: Size) {
        canvas.drawRect(
            paint = Paint().apply { this.color = Color.White },
            rect = Rect(Offset(0f, 0f), size),
        )

        canvas.drawRect(
            paint = Paint().apply {
                shader = LinearGradientShader(
                    from = Offset(0f, 0f),
                    to = Offset(size.width, 0f),
                    colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red),
                )
            },
            rect = Rect(Offset(0f, 0f), size),
        )

        canvas.drawRect(
            paint = Paint().apply {
                shader = LinearGradientShader(
                    from = Offset(0f, 0f),
                    to = Offset(0f, size.height),
                    colors = listOf(Color.Transparent, Color.Black),
                )
            },
            rect = Rect(Offset(0f, 0f), size),
        )

        canvas.drawRect(
            paint = Paint().apply {
                shader = LinearGradientShader(
                    from = Offset(0f, 0f),
                    to = Offset(0f, size.height),
                    colors = listOf(Color.White, Color.Black),
                )
                this.alpha = 1f - color.saturation
            },
            rect = Rect(Offset(0f, 0f), size),
        )
    }

    override fun colorForOffset(
        color: KmpColor,
        offset: Offset,
        size: IntSize
    ): KmpColor {
        val hue = ((360f / size.width) * offset.x).coerceIn(0f..360f)
        val value = (100f - ((100f / size.height) * offset.y).coerceIn(0f..100f)) / 100f
        return HsvColor(hue, color.saturation, value)
    }

    override fun offsetForColor(
        color: Color,
        size: IntSize
    ): Offset {
        TODO("Not yet implemented")
    }

}

val HsDrawer = object : IKmpColorPickerDrawer {
    override fun drawFunc(color: KmpColor, canvas: Canvas, size: Size) {
        canvas.drawRect(
            paint = Paint().apply { this.color = Color.White },
            rect = Rect(Offset(0f, 0f), size),
        )

        canvas.drawRect(
            paint = Paint().apply {
                shader = LinearGradientShader(
                    from = Offset(0f, 0f),
                    to = Offset(size.width, 0f),
                    colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red),
                )
            },
            rect = Rect(Offset(0f, 0f), size),
        )

        canvas.drawRect(
            paint = Paint().apply {
                shader = LinearGradientShader(
                    from = Offset(0f, 0f),
                    to = Offset(0f, size.height),
                    colors = listOf(Color.White, Color.Transparent),
                )
            },
            rect = Rect(Offset(0f, 0f), size),
        )

        canvas.drawRect(
            paint = Paint().apply {
                this.color = Color.Black
                this.alpha = 1f - color.value
            },
            rect = Rect(Offset(0f, 0f), size),
        )
    }

    override fun colorForOffset(
        color: KmpColor,
        offset: Offset,
        size: IntSize
    ): KmpColor {
        val hue = ((360f / size.width) * offset.x).coerceIn(0f..360f)
        val saturation = ((100f / size.height) * offset.y).coerceIn(0f..100f) / 100f
        return HsvColor(hue, saturation, color.value)
    }

    override fun offsetForColor(
        color: Color,
        size: IntSize
    ): Offset {
        TODO("Not yet implemented")
    }

}

val HsCircleDrawer = object : IKmpColorPickerDrawer {
    override fun drawFunc(color: KmpColor, canvas: Canvas, size: Size) {
        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply { this.color = Color.White },
            radius = size.width / 2f,
        )

        canvas.rotate(90f, size.center.x, size.center.y)

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                shader = SweepGradientShader(
                    center = size.center,
                    colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red),
                )
            },
            radius = size.width / 2f,
        )

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                shader = RadialGradientShader(
                    center = size.center,
                    radius = size.width / 2f,
                    colors = listOf(Color.White, Color.Transparent),
                )
            },
            radius = size.width / 2f,
        )

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                this.color = Color.Black
                alpha = 1f - color.value
            },
            radius = size.width / 2f,
        )
    }

    override fun colorForOffset(
        color: KmpColor,
        offset: Offset,
        size: IntSize
    ): KmpColor {
        val centerX: Double = size.width / 2.0
        val centerY: Double = size.height / 2.0
        val radius: Double = min(centerX, centerY)
        val xOffset: Double = offset.x - centerX
        val yOffset: Double = offset.y - centerY
        val centerOffset = hypot(xOffset, yOffset)
        val rawAngle = atan2(yOffset, xOffset).toDegree() - 90f
        val centerAngle = (rawAngle + 360.0) % 360.0
        return HsvColor(centerAngle.toFloat(), (centerOffset / radius).toFloat(), color.value)
    }

    override fun offsetForColor(
        color: Color,
        size: IntSize
    ): Offset {
        TODO("Not yet implemented")
    }

}

val HvCircleDrawer = object : IKmpColorPickerDrawer {
    override fun drawFunc(color: KmpColor, canvas: Canvas, size: Size) {
        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply { this.color = Color.White },
            radius = size.width / 2f,
        )

        canvas.rotate(90f, size.center.x, size.center.y)

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                shader = SweepGradientShader(
                    center = size.center,
                    colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red),
                )
            },
            radius = size.width / 2f,
        )

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                shader = RadialGradientShader(
                    center = size.center,
                    radius = size.width / 2f,
                    colors = listOf(Color.Black, Color.Transparent),
                )
            },
            radius = size.width / 2f,
        )

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                shader = RadialGradientShader(
                    center = size.center,
                    radius = size.width / 2f,
                    colors = listOf(Color.Black, Color.White),
                )
                this.alpha = 1f - color.saturation
            },
            radius = size.width / 2f,
        )
    }

    override fun colorForOffset(
        color: KmpColor,
        offset: Offset,
        size: IntSize
    ): KmpColor {
        val centerX: Double = size.width / 2.0
        val centerY: Double = size.height / 2.0
        val radius: Double = min(centerX, centerY)
        val xOffset: Double = offset.x - centerX
        val yOffset: Double = offset.y - centerY
        val centerOffset = hypot(xOffset, yOffset)
        val rawAngle = atan2(yOffset, xOffset).toDegree() - 90f
        val centerAngle = (rawAngle + 360.0) % 360.0
        return HsvColor(centerAngle.toFloat(), color.saturation, (centerOffset / radius).toFloat())
    }

    override fun offsetForColor(
        color: Color,
        size: IntSize
    ): Offset {
        TODO("Not yet implemented")
    }

}