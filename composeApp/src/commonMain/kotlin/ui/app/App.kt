package ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInject
import com.outsidesource.oskitcompose.systemui.SystemBarColorEffect
import com.outsidesource.oskitcompose.systemui.SystemBarIconColor
import org.koin.core.parameter.parametersOf
import kotlin.math.*

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

    var color by remember { mutableStateOf<HsvColor>(HsvColor(0f, 1f, 1f)) }
    val colorFunc: (HsvColor) -> Unit = { color = it }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
    ) {
        Row {
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                color = color,
                renderer = HvDrawer,
                onChange = colorFunc
            )
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                color = color,
                renderer = HsDrawer,
                onChange = colorFunc
            )
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                color = color,
                renderer = SvDrawer,
                onChange = colorFunc
            )
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                color = color,
                renderer = HsCircleDrawer,
                onChange = colorFunc
            )
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                color = color,
                renderer = HvCircleDrawer,
                onChange = colorFunc
            )
            Box(modifier = Modifier.size(100.dp).background(color.toColor()))
        }
        Column(
            modifier = Modifier.width(200.dp)
        ) {
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = color.hue,
                valueRange = 0f..360f,
                onValueChange = {
                    color = HsvColor(it, color.saturation, color.value)
                },
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = color.saturation * 100f,
                valueRange = 0f..100f,
                onValueChange = {
                    color = HsvColor(color.hue, it / 100f, color.value)
                },
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = color.value * 100f,
                valueRange = 0f..100f,
                onValueChange = {
                    color = HsvColor(color.hue, color.saturation, it / 100f)
                },
            )
        }
    }
}

/**
 * Represents HSV color (hue, saturation, brightness)
 *
 * @param hue 0-360
 * @param saturation 0-1
 * @param value 0-1
 * @param alpha 0-1
 */
data class HsvColor(
    val hue: Float,
    val saturation: Float,
    val value: Float,
    val alpha: Float = 1f,
) {

    init {
        require(hue >= 0 && hue <= 360) { "Hue must be between 0 and 360" }
        require(saturation >= 0f && saturation <= 1f) { "Saturation must be between 0f and 1f" }
        require(value >= 0f && value <= 1f) { "Value must be between 0f and 1f" }
        require(alpha >= 0f && alpha <= 1f) { "Alpha must be between 0f and 1f" }
    }

    fun toColor(): Color = hsvToRgb(hue, saturation, value, alpha)

    companion object {
        fun fromColor(color: Color) = color.toHsvColor()
    }
}

fun Color.toHsvColor(): HsvColor = rgbToHsv(red, green, blue, alpha)

@Composable
fun KmpColorPicker(
    color: HsvColor,
    renderer: IKmpColorPickerRenderer,
    onChange: (HsvColor) -> Unit,
    handle: @Composable (color: HsvColor) -> Unit = { KmpColorPickerHandle(color) },
    modifier: Modifier = Modifier,
) {
    val localColor = rememberUpdatedState(color)

    // TODO: Maybe make a local color that updates regardless of the outside value if the user is dragging

    BoxWithConstraints(
        modifier = Modifier
            .defaultMinSize(minWidth = 100.dp, minHeight = 100.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { onChange(renderer.colorForOffset(localColor.value, it, size)) }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, _ -> onChange(renderer.colorForOffset(localColor.value, change.position, size)) }
                )
            }
            .drawBehind {
                drawIntoCanvas { renderer.drawFunc(localColor.value, it, size) }
            }
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    val offset = renderer.offsetForColor(localColor.value, IntSize(constraints.maxWidth, constraints.maxHeight))
                    translationX = offset.x - (size.width / 2)
                    translationY = offset.y - (size.width / 2)
                }
        ) {
            handle(color)
        }
    }
}

@Composable
fun KmpColorPickerHandle(color: HsvColor) {
    Box(modifier = Modifier
        .size(40.dp)
        .border(2.dp, color = Color.White, CircleShape)
        .background(color.toColor(), CircleShape)
    )
}

fun Double.toDegree(): Double = this * 180.0 / PI.toDouble()
fun Float.toRadians(): Double = this * PI.toDouble() / 180.0

fun hsvToRgb(h: Float, s: Float, v: Float, a: Float): Color {
    val aNorm = (a * 255).roundToInt().coerceIn(0, 255)
    val hNorm = ((h % 360) + 360) % 360
    if (s < 1e-6f) {
        val gray = (v * 255).roundToInt().coerceIn(0, 255)
        return Color(gray, gray, gray, aNorm)
    }

    val hPrime = hNorm / 60f
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

    return Color(r, g, b, aNorm)
}

fun rgbToHsv(r: Float, g: Float, b: Float, a: Float): HsvColor {
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min

    var h = when {
        delta == 0f -> 0f
        max == r -> 60 * (((g - b) / delta) % 6)
        max == g -> 60 * (((b - r) / delta) + 2)
        else -> 60 * (((r - g) / delta) + 4)
    }
    if (h < 0) h += 360f

    val s = if (max == 0f) 0f else delta / max
    val v = max

    return HsvColor(h.coerceIn(0f, 360f), s.coerceIn(0f, 1f), v.coerceIn(0f, 1f), a)
}

interface IKmpColorPickerRenderer {
    fun drawFunc(color: HsvColor, canvas: Canvas, size: Size)
    fun colorForOffset(color: HsvColor, offset: Offset, size: IntSize): HsvColor
    fun offsetForColor(color: HsvColor, size: IntSize): Offset
}

val SvDrawer = object : IKmpColorPickerRenderer {
    override fun drawFunc(color: HsvColor, canvas: Canvas, size: Size) {
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
        color: HsvColor,
        offset: Offset,
        size: IntSize
    ): HsvColor {
        val saturation = ((100f / size.width) * offset.x).coerceIn(0f..100f) / 100f
        val value = (100f - ((100f / size.height) * offset.y).coerceIn(0f..100f)) / 100f
        return HsvColor(color.hue, saturation, value)
    }

    override fun offsetForColor(
        color: HsvColor,
        size: IntSize
    ): Offset {
        val x = (size.width * color.saturation).coerceIn(0f, size.width.toFloat())
        val y = (size.height * (1f - color.value)).coerceIn(0f, size.height.toFloat())
        return Offset(x, y)
    }
}

val HvDrawer = object : IKmpColorPickerRenderer {
    override fun drawFunc(color: HsvColor, canvas: Canvas, size: Size) {
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
        color: HsvColor,
        offset: Offset,
        size: IntSize
    ): HsvColor {
        val hue = ((360f / size.width) * offset.x).coerceIn(0f..360f)
        val value = (100f - ((100f / size.height) * offset.y).coerceIn(0f..100f)) / 100f
        return HsvColor(hue, color.saturation, value)
    }

    override fun offsetForColor(
        color: HsvColor,
        size: IntSize
    ): Offset {
        val x = (size.width * (color.hue / 360f)).coerceIn(0f, size.width.toFloat())
        val y = (size.height * (1f - color.value)).coerceIn(0f, size.height.toFloat())
        return Offset(x, y)
    }
}

val HsDrawer = object : IKmpColorPickerRenderer {
    override fun drawFunc(color: HsvColor, canvas: Canvas, size: Size) {
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
        color: HsvColor,
        offset: Offset,
        size: IntSize
    ): HsvColor {
        val hue = ((360f / size.width) * offset.x).coerceIn(0f..360f)
        val saturation = ((100f / size.height) * offset.y).coerceIn(0f..100f) / 100f
        return HsvColor(hue, saturation, color.value)
    }

    override fun offsetForColor(
        color: HsvColor,
        size: IntSize
    ): Offset {
        val x = (size.width * (color.hue / 360f)).coerceIn(0f, size.width.toFloat())
        val y = (size.height * color.saturation).coerceIn(0f, size.height.toFloat())
        return Offset(x, y)
    }

}

val HsCircleDrawer = object : IKmpColorPickerRenderer {
    override fun drawFunc(color: HsvColor, canvas: Canvas, size: Size) {
        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply { this.color = Color.White },
            radius = size.width / 2f,
        )

        canvas.save()
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
        canvas.restore()
    }

    override fun colorForOffset(
        color: HsvColor,
        offset: Offset,
        size: IntSize
    ): HsvColor {
        val centerX: Double = size.width / 2.0
        val centerY: Double = size.height / 2.0
        val radius: Double = min(centerX, centerY)
        val xOffset: Double = offset.x - centerX
        val yOffset: Double = offset.y - centerY
        val centerOffset = hypot(xOffset, yOffset)
        val rawAngle = atan2(yOffset, xOffset).toDegree() - 90f
        val centerAngle = (rawAngle + 360.0) % 360.0
        return HsvColor(centerAngle.toFloat().coerceIn(0f, 360f), (centerOffset / radius).toFloat().coerceIn(0f, 1f), color.value)
    }

    override fun offsetForColor(
        color: HsvColor,
        size: IntSize
    ): Offset {
        val theta = (color.hue + 90f).toRadians()
        val polarRadius = (size.width / 2f) * color.saturation
        val x = polarRadius * cos(theta)
        val y = polarRadius * sin(theta)
        return Offset((size.width / 2f) + x.toFloat(), (size.height / 2f) + y.toFloat())
    }
}

val HvCircleDrawer = object : IKmpColorPickerRenderer {
    override fun drawFunc(color: HsvColor, canvas: Canvas, size: Size) {
        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply { this.color = Color.White },
            radius = size.width / 2f,
        )

        canvas.save()
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
        canvas.restore()
    }

    override fun colorForOffset(
        color: HsvColor,
        offset: Offset,
        size: IntSize
    ): HsvColor {
        val centerX: Double = size.width / 2.0
        val centerY: Double = size.height / 2.0
        val radius: Double = min(centerX, centerY)
        val xOffset: Double = offset.x - centerX
        val yOffset: Double = offset.y - centerY
        val centerOffset = hypot(xOffset, yOffset)
        val rawAngle = atan2(yOffset, xOffset).toDegree() - 90f
        val centerAngle = (rawAngle + 360.0) % 360.0
        return HsvColor(centerAngle.toFloat().coerceIn(0f, 360f), color.saturation, (centerOffset / radius).toFloat().coerceIn(0f, 1f))
    }

    override fun offsetForColor(
        color: HsvColor,
        size: IntSize
    ): Offset {
        val theta = (color.hue + 90f).toRadians()
        val polarRadius = (size.width / 2f) * color.value
        val x = polarRadius * cos(theta)
        val y = polarRadius * sin(theta)
        return Offset((size.width / 2f) + x.toFloat(), (size.height / 2f) + y.toFloat())
    }
}