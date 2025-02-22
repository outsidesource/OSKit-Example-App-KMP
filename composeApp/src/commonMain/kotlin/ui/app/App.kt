package ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInject
import com.outsidesource.oskitcompose.modifier.outerShadow
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
    var colors = remember {
        mutableStateMapOf(
            "1" to HsvColor(0f, 1f, 1f),
            "2" to HsvColor(90f, 1f, 1f),
            "3" to HsvColor(180f, 1f, 1f),
            "4" to HsvColor(270f, 1f, 1f),
        )
    }
    val colorFunc: (HsvColor, Any, Any) -> Unit = { c, _, _ -> color = c }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
    ) {
        Row {
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                color = color,
                renderer = HvColorPickerRenderer,
                onChange = colorFunc,
            )
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                color = color,
                renderer = HsColorPickerRenderer,
                onChange = colorFunc,
            )
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                color = color,
                renderer = SvColorPickerRenderer,
                onChange = colorFunc,
            )
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                color = color,
                renderer = HsCircleColorPickerRenderer,
                onChange = colorFunc,
            )
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                color = color,
                renderer = HvCircleColorPickerRenderer,
                onChange = colorFunc,
            )
            KmpColorPicker(
                modifier = Modifier.size(200.dp),
                colors = colors,
                renderer = HsCircleColorPickerRenderer,
                onChange = { key, color, _, _ -> colors[key] = color },
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
                    color = color.copy(hue = it)
                },
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = color.saturation * 100f,
                valueRange = 0f..100f,
                onValueChange = {
                    color = color.copy(saturation = it / 100f)
                },
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = color.value * 100f,
                valueRange = 0f..100f,
                onValueChange = {
                    color = color.copy(value = it / 100f)
                },
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = color.alpha * 100f,
                valueRange = 0f..100f,
                onValueChange = {
                    color = color.copy(alpha = it / 100f)
                },
            )
        }
    }
}


@Composable
fun KmpColorPicker(
    color: HsvColor,
    renderer: IKmpColorPickerRenderer = SvColorPickerRenderer,
    onChange: (HsvColor, offset: Offset, size: IntSize) -> Unit = { _, _, _ -> },
    onDone: (HsvColor, offset: Offset, size: IntSize) -> Unit = { _, _, _ -> },
    handle: @Composable (color: HsvColor) -> Unit = { KmpColorPickerHandle(it) },
    modifier: Modifier = Modifier,
) {
    var draggingColor by remember { mutableStateOf(color) }
    var isDragging by remember { mutableStateOf(false) }
    val mergedColor = rememberUpdatedState(if (isDragging) draggingColor else color)

    BoxWithConstraints(
        modifier = Modifier
            .defaultMinSize(minWidth = 100.dp, minHeight = 100.dp)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val downColor = renderer.colorForOffset(mergedColor.value, down.position, size)
                    draggingColor = downColor
                    isDragging = true
                    onChange(downColor, down.position, size)

                    var lastPosition = down.position
                    drag(down.id) {
                        lastPosition = it.position
                        val updatedColor = renderer.colorForOffset(mergedColor.value, it.position, size)
                        draggingColor = updatedColor
                        onChange(updatedColor, it.position, size)
                    }

                    val upColor = renderer.colorForOffset(mergedColor.value, lastPosition, size)
                    isDragging = false
                    onDone(upColor, lastPosition, size)
                }
            }
            .drawBehind {
                drawIntoCanvas { renderer.draw(mergedColor.value, it, size) }
            }
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    val offset = renderer.offsetForColor(mergedColor.value, IntSize(constraints.maxWidth, constraints.maxHeight))
                    translationX = offset.x - (size.width / 2)
                    translationY = offset.y - (size.width / 2)
                }
        ) {
            handle(mergedColor.value)
        }
    }
}

@Composable
fun KmpColorPicker(
    colors: Map<String, HsvColor>,
    renderer: IKmpColorPickerRenderer = SvColorPickerRenderer,
    onChange: (String, HsvColor, offset: Offset, size: IntSize) -> Unit = { _, _, _, _ -> },
    onDone: (String, HsvColor, offset: Offset, size: IntSize) -> Unit = { _, _, _, _ -> },
    handle: @Composable (color: HsvColor) -> Unit = { KmpColorPickerHandle(it) },
    modifier: Modifier = Modifier,
) {
    var draggingColor by remember { mutableStateOf<HsvColor?>(null) }
    var draggingKey by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .defaultMinSize(minWidth = 100.dp, minHeight = 100.dp)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val activeKey = run {
                        var minDistance = Pair<String?, Float>(null, Float.MAX_VALUE)
                        colors.forEach { (key, color) ->
                            val distance = cartesianDistance(down.position, renderer.offsetForColor(color, size))
                            if (distance > minDistance.second) return@forEach
                            minDistance = Pair(key, distance)
                        }
                        minDistance.first ?: return@awaitEachGesture
                    }

                    val downColor = renderer.colorForOffset(colors[activeKey] ?: HsvColor.Black, down.position, size)
                    draggingColor = downColor
                    draggingKey = activeKey
                    onChange(activeKey, downColor, down.position, size)

                    var lastPosition = down.position
                    drag(down.id) {
                        lastPosition = it.position
                        val updatedColor = renderer.colorForOffset(draggingColor ?: HsvColor.Black, it.position, size)
                        draggingColor = updatedColor
                        onChange(activeKey, updatedColor, it.position, size)
                    }

                    val upColor = renderer.colorForOffset(draggingColor ?: HsvColor.Black, lastPosition, size)
                    draggingKey = null
                    onDone(activeKey, upColor, lastPosition, size)
                }
            }
            .drawBehind {
                drawIntoCanvas { renderer.draw(colors.values.firstOrNull() ?: HsvColor.Black, it, size) }
            }
            .then(modifier)
    ) {
        colors.forEach { (key, color) ->
            val mergedColor = rememberUpdatedState(if (draggingKey == key) draggingColor ?: color else color)

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        val offset = renderer.offsetForColor(mergedColor.value, IntSize(constraints.maxWidth, constraints.maxHeight))
                        translationX = offset.x - (size.width / 2)
                        translationY = offset.y - (size.width / 2)
                    }
//                    .pointerInput(Unit) {
//                        awaitEachGesture {
//                            val down = awaitFirstDown(requireUnconsumed = false)
//                            val parentSize = IntSize(constraints.maxWidth, constraints.maxHeight)
//                            val parentOffset = renderer.offsetForColor(mergedColor.value, parentSize)
//                            var offset = parentOffset + down.position - Offset(size.width / 2f,  size.height / 2f)
//
//                            val downColor = renderer.colorForOffset(mergedColor.value, offset, parentSize)
//                            draggingColor = downColor
//                            draggingKey = key
//                            onChange(key, downColor, offset, parentSize)
//
//                            drag(down.id) {
//                                offset = offset + (it.position - it.previousPosition)
//                                val dragColor = renderer.colorForOffset(mergedColor.value, offset, parentSize)
//                                draggingColor = dragColor
//                                onChange(key, dragColor, offset, parentSize)
//                            }
//
//                            val upColor = renderer.colorForOffset(mergedColor.value, offset, size)
//                            draggingKey = null
//                            onDone(key, upColor, offset, size)
//                        }
//                    }
            ) {
                handle(mergedColor.value)
            }
        }
    }
}

fun cartesianDistance(p1: Offset, p2: Offset): Float {
    val dx = p2.x - p1.x
    val dy = p2.y - p1.y
    return sqrt(dx.pow(2) + dy.pow(2))
}

@Composable
fun KmpColorPickerHandle(
    color: HsvColor,
    modifier: Modifier = Modifier.size(30.dp),
) {
    Box(
        modifier = modifier
            .border(2.dp, color = Color.White, CircleShape)
            .outerShadow(
                blur = 2.dp,
                offset = DpOffset(0.dp, 1.dp),
                color = Color.Black.copy(alpha = .5f),
                shape = CircleShape,
            )
            .background(Color.White, CircleShape)
            .background(color.toColor(), CircleShape)
    )
}

/**
 * Represents HSV color (hue, saturation, brightness)
 *
 * @param hue 0-360
 * @param saturation 0-1
 * @param value 0-1
 * @param alpha 0-1
 */
@Immutable
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

    fun toColor(): Color {
        val aNorm = (alpha * 255).roundToInt().coerceIn(0, 255)
        val hNorm = ((hue % 360) + 360) % 360
        if (saturation < 1e-6f) {
            val gray = (value * 255).roundToInt().coerceIn(0, 255)
            return Color(gray, gray, gray, aNorm)
        }

        val hPrime = hNorm / 60f
        val sector = hPrime.toInt()
        val fraction = hPrime - sector

        val p = value * (1 - saturation)
        val q = value * (1 - saturation * fraction)
        val t = value * (1 - saturation * (1 - fraction))

        val (rf, gf, bf) = when (sector % 6) {
            0 -> Triple(value, t, p)
            1 -> Triple(q, value, p)
            2 -> Triple(p, value, t)
            3 -> Triple(p, q, value)
            4 -> Triple(t, p, value)
            5 -> Triple(value, p, q)
            else -> Triple(0f, 0f, 0f)
        }

        val r = (rf * 255).roundToInt().coerceIn(0, 255)
        val g = (gf * 255).roundToInt().coerceIn(0, 255)
        val b = (bf * 255).roundToInt().coerceIn(0, 255)

        return Color(r, g, b, aNorm)
    }

    companion object {
        val Black = HsvColor(0f, 0f, 0f)
        val White = HsvColor(0f, 0f, 1f)
        val Transparent = HsvColor(0f, 0f, 0f, 0f)
        val Red = HsvColor(0f, 1f, 1f)
        val Yellow = HsvColor(60f, 1f, 1f)
        val Green = HsvColor(120f, 1f, 1f)
        val Cyan = HsvColor(180f, 1f, 1f)
        val Blue = HsvColor(240f, 1f, 1f)
        val Magenta = HsvColor(300f, 1f, 1f)
        val Gray = HsvColor(0f, 0f, 0.53333336f)

        fun fromColor(color: Color) = color.toHsvColor()
    }
}

fun Color.toHsvColor(): HsvColor {
    val max = maxOf(red, green, blue)
    val min = minOf(red, green, blue)
    val delta = max - min

    var h = when {
        delta == 0f -> 0f
        max == red-> 60 * (((green - blue) / delta) % 6)
        max == green -> 60 * (((blue - red) / delta) + 2)
        else -> 60 * (((red - green) / delta) + 4)
    }
    if (h < 0) h += 360f

    val hNorm = (h + 360) % 360
    val s = if (max == 0f) 0f else delta / max
    val v = max

    return HsvColor(hNorm, s.coerceIn(0f, 1f), v.coerceIn(0f, 1f), alpha)
}

fun Double.toDegree(): Double = this * 180.0 / PI.toDouble()
fun Float.toRadians(): Double = this * PI.toDouble() / 180.0

interface IKmpColorPickerRenderer {
    fun draw(color: HsvColor, canvas: Canvas, size: Size)
    fun colorForOffset(color: HsvColor, offset: Offset, size: IntSize): HsvColor
    fun offsetForColor(color: HsvColor, size: IntSize): Offset
}

object SvColorPickerRenderer : IKmpColorPickerRenderer {
    override fun draw(color: HsvColor, canvas: Canvas, size: Size) {
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

        canvas.drawRect(
            paint = Paint().apply {
                blendMode = BlendMode.Xor
                alpha = 1f - color.alpha
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
        return color.copy(saturation = saturation, value = value)
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

object HvColorPickerRenderer : IKmpColorPickerRenderer {
    override fun draw(color: HsvColor, canvas: Canvas, size: Size) {
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
                alpha = 1f - color.saturation
            },
            rect = Rect(Offset(0f, 0f), size),
        )

        canvas.drawRect(
            paint = Paint().apply {
                blendMode = BlendMode.Xor
                alpha = 1f - color.alpha
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
        return color.copy(hue = hue, value = value)
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

object HsColorPickerRenderer : IKmpColorPickerRenderer {
    override fun draw(color: HsvColor, canvas: Canvas, size: Size) {
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
                alpha = 1f - color.value
            },
            rect = Rect(Offset(0f, 0f), size),
        )

        canvas.drawRect(
            paint = Paint().apply {
                blendMode = BlendMode.Xor
                alpha = 1f - color.alpha
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
        return color.copy(hue = hue, saturation = saturation)
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

object HsCircleColorPickerRenderer : IKmpColorPickerRenderer {
    override fun draw(color: HsvColor, canvas: Canvas, size: Size) {
        val radius = min(size.width, size.height) / 2f

        canvas.drawCircle(
            paint = Paint().apply { this.color = Color.White },
            center = size.center,
            radius = radius,
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
            radius = radius,
        )

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                shader = RadialGradientShader(
                    center = size.center,
                    radius = radius,
                    colors = listOf(Color.White, Color.Transparent),
                )
            },
            radius = radius,
        )

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                this.color = Color.Black
                alpha = 1f - color.value
            },
            radius = radius,
        )

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                blendMode = BlendMode.Xor
                alpha = 1f - color.alpha
            },
            radius = radius,
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
        val circleRadius: Double = min(centerX, centerY)
        val xOffset: Double = offset.x - centerX
        val yOffset: Double = offset.y - centerY
        val radius = hypot(xOffset, yOffset)
        val rawAngle = atan2(yOffset, xOffset).toDegree() - 90f
        val normalizedAngle = (rawAngle + 360.0) % 360.0
        return color.copy(hue = normalizedAngle.toFloat(), saturation = (radius / circleRadius).toFloat().coerceIn(0f, 1f))
    }

    override fun offsetForColor(
        color: HsvColor,
        size: IntSize
    ): Offset {
        val circleRadius = min(size.height, size.width) / 2f
        val theta = (color.hue + 90f).toRadians()
        val polarRadius = circleRadius * color.saturation
        val x = polarRadius * cos(theta)
        val y = polarRadius * sin(theta)
        return Offset((size.width / 2f) + x.toFloat(), (size.height / 2f) + y.toFloat())
    }
}

object HvCircleColorPickerRenderer : IKmpColorPickerRenderer {
    override fun draw(color: HsvColor, canvas: Canvas, size: Size) {
        val radius = min(size.width, size.height) / 2f

        canvas.drawCircle(
            paint = Paint().apply { this.color = Color.White },
            center = size.center,
            radius = radius,
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
            radius = radius,
        )

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                shader = RadialGradientShader(
                    center = size.center,
                    radius = radius,
                    colors = listOf(Color.Black, Color.Transparent),
                )
            },
            radius = radius,
        )

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                shader = RadialGradientShader(
                    center = size.center,
                    radius = radius,
                    colors = listOf(Color.Black, Color.White),
                )
                alpha = 1f - color.saturation
            },
            radius = radius,
        )

        canvas.drawCircle(
            center = size.center,
            paint = Paint().apply {
                blendMode = BlendMode.Xor
                alpha = 1f - color.alpha
            },
            radius = radius,
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
        val circleRadius: Double = min(centerX, centerY)
        val cartesianX: Double = offset.x - centerX
        val cartesianY: Double = offset.y - centerY
        val radius = hypot(cartesianX, cartesianY)
        val rawAngle = atan2(cartesianY, cartesianX).toDegree() - 90f
        val normalizedAngle = (rawAngle + 360.0) % 360.0
        return color.copy(hue = normalizedAngle.toFloat(), value = (radius / circleRadius).toFloat().coerceIn(0f, 1f))
    }

    override fun offsetForColor(
        color: HsvColor,
        size: IntSize
    ): Offset {
        val circleRadius = min(size.height, size.width) / 2f
        val theta = (color.hue + 90f).toRadians()
        val polarRadius = circleRadius * color.value
        val x = polarRadius * cos(theta)
        val y = polarRadius * sin(theta)
        return Offset((size.width / 2f) + x.toFloat(), (size.height / 2f) + y.toFloat())
    }
}