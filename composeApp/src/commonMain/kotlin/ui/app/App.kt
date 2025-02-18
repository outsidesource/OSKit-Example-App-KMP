package ui.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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

    var color by remember { mutableStateOf(Color.Black) }
    var calculatedColor by remember { mutableStateOf(Color.Black) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
    ) {
        ColorPicker(hvDrawer) { imageColor, calcColor ->
            color = imageColor
            calculatedColor = calcColor
            println("Image: ${255 * color.red},${255 * color.green},${255 * color.blue}")
            println("Calculated: ${255 * calculatedColor.red},${255 * calculatedColor.green},${255 * calculatedColor.blue}")
        }
        ColorPicker(hsDrawer) {imageColor, calcColor ->
            color = imageColor
            calculatedColor = calcColor
            println("Image: ${255 * color.red},${255 * color.green},${255 * color.blue}")
            println("Calculated: ${255 * calculatedColor.red},${255 * calculatedColor.green},${255 * calculatedColor.blue}")
        }
        ColorPicker(svDrawer) {imageColor, calcColor ->
            color = imageColor
            calculatedColor = calcColor
            println("Image: ${255 * color.red},${255 * color.green},${255 * color.blue}")
            println("Calculated: ${255 * calculatedColor.red},${255 * calculatedColor.green},${255 * calculatedColor.blue}")
        }
        ColorPicker(hsCircleDrawer) {imageColor, calcColor ->
            color = imageColor
            calculatedColor = calcColor
            println("Image: ${255 * color.red},${255 * color.green},${255 * color.blue}")
            println("Calculated: ${255 * calculatedColor.red},${255 * calculatedColor.green},${255 * calculatedColor.blue}")
        }
        ColorPicker(hvCircleDrawer) {imageColor, calcColor ->
            color = imageColor
            calculatedColor = calcColor
            println("Image: ${255 * color.red},${255 * color.green},${255 * color.blue}")
            println("Calculated: ${255 * calculatedColor.red},${255 * calculatedColor.green},${255 * calculatedColor.blue}")
        }
        Column {
            Box(modifier = Modifier.size(100.dp).background(color))
            Box(modifier = Modifier.size(100.dp).background(calculatedColor))
        }
        // TODO: Test calculated color vs picked color
        // I can't do clicked color exclusively because I couldn't do both RGB and HSV natively. Only RGB would be native due to the pixel data being RGB
    }
}

// TODO: need to be able to look up a location based on color

@Composable
fun ColorPicker(
    drawer: (Canvas, Size) -> Unit,
    onChange: (Color, Color) -> Unit,
) {
    val density = LocalDensity.current
    val bitmap = remember {
        val bitmap = with(density) {
            ImageBitmap(200.dp.toPx().toInt(), 200.dp.toPx().toInt())
        }

        val canvas = Canvas(bitmap)
        drawer(canvas, Size(bitmap.width.toFloat(), bitmap.height.toFloat()))
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
                    onChange(Color(pixel.buffer[0]), colorForDrawerAndPosition(drawer, it, size))
                })
            }
    ) {
        drawIntoCanvas { drawer(it, size) }
    }
}

//fun positionForColor(drawer: (Canvas, Size) -> Unit, color: Color, size: IntSize): Offset {
//    return when (drawer) {
//        hvDrawer -> {
//            val hue = ((360f / size.width) * offset.x).coerceIn(0f..360f)
//            val saturation = (100f) / 100f
//            val value = (100f - ((100f / size.height) * offset.y).coerceIn(0f..100f)) / 100f
//            Offset(0f, 0f)
//        }
//        hsDrawer -> {
//            val hue = ((360f / size.width) * offset.x).coerceIn(0f..360f)
//            val saturation = ((100f / size.height) * offset.y).coerceIn(0f..100f) / 100f
//            val value = (100f) / 100f
//            Offset(0f, 0f)
//        }
//        svDrawer -> {
//            val hue = 360f
//            val saturation = ((100f / size.width) * offset.x).coerceIn(0f..100f) / 100f
//            val value = (100f - ((100f / size.height) * offset.y).coerceIn(0f..100f)) / 100f
//            Offset(0f, 0f)
//        }
//        hsCircleDrawer -> {
//            val centerX: Double = size.width / 2.0
//            val centerY: Double = size.height / 2.0
//            val radius: Double = min(centerX, centerY)
//            val xOffset: Double = offset.x - centerX
//            val yOffset: Double = offset.y - centerY
//            val centerOffset = hypot(xOffset, yOffset)
//            val rawAngle = atan2(yOffset, xOffset).toDegree() - 90f
//            val centerAngle = (rawAngle + 360.0) % 360.0
//            Offset(0f, 0f)
//        }
//        hvCircleDrawer -> {
//            val centerX: Double = size.width / 2.0
//            val centerY: Double = size.height / 2.0
//            val radius: Double = min(centerX, centerY)
//            val xOffset: Double = offset.x - centerX
//            val yOffset: Double = offset.y - centerY
//            val centerOffset = hypot(xOffset, yOffset)
//            val rawAngle = atan2(yOffset, xOffset).toDegree() - 90f
//            val centerAngle = (rawAngle + 360.0) % 360.0
//            Offset(0f, 0f)
//        }
//        else -> Offset(0f, 0f)
//    }
//}

fun colorForDrawerAndPosition(drawer: (Canvas, Size) -> Unit, offset: Offset, size: IntSize): Color {
    return when (drawer) {
        hvDrawer -> {
            val hue = ((360f / size.width) * offset.x).coerceIn(0f..360f)
            val saturation = (100f) / 100f
            val value = (100f - ((100f / size.height) * offset.y).coerceIn(0f..100f)) / 100f
            return hsvToRgb(hue, saturation, value)
        }
        hsDrawer -> {
            val hue = ((360f / size.width) * offset.x).coerceIn(0f..360f)
            val saturation = ((100f / size.height) * offset.y).coerceIn(0f..100f) / 100f
            val value = (100f) / 100f
            return hsvToRgb(hue, saturation, value)
        }
        svDrawer -> {
            val hue = 360f
            val saturation = ((100f / size.width) * offset.x).coerceIn(0f..100f) / 100f
            val value = (100f - ((100f / size.height) * offset.y).coerceIn(0f..100f)) / 100f
            return hsvToRgb(hue, saturation, value)
        }
        hsCircleDrawer -> {
            val centerX: Double = size.width / 2.0
            val centerY: Double = size.height / 2.0
            val radius: Double = min(centerX, centerY)
            val xOffset: Double = offset.x - centerX
            val yOffset: Double = offset.y - centerY
            val centerOffset = hypot(xOffset, yOffset)
            val rawAngle = atan2(yOffset, xOffset).toDegree() - 90f
            val centerAngle = (rawAngle + 360.0) % 360.0
            return hsvToRgb(centerAngle.toFloat(), (centerOffset / radius).toFloat(), 1f)
        }
        hvCircleDrawer -> {
            val centerX: Double = size.width / 2.0
            val centerY: Double = size.height / 2.0
            val radius: Double = min(centerX, centerY)
            val xOffset: Double = offset.x - centerX
            val yOffset: Double = offset.y - centerY
            val centerOffset = hypot(xOffset, yOffset)
            val rawAngle = atan2(yOffset, xOffset).toDegree() - 90f
            val centerAngle = (rawAngle + 360.0) % 360.0
            return hsvToRgb(centerAngle.toFloat(), 1f, (centerOffset / radius).toFloat())
        }
        else -> Color.Black
    }
}

fun Double.toDegree(): Double = this * 180.0 / PI.toDouble()

fun hsvToRgb(h: Float, s: Float, v: Float): Color {
    if (s == 0f) {
        val gray = (v * 255).toInt().coerceIn(0, 255)
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

    val r = (rf * 255).toInt().coerceIn(0, 255)
    val g = (gf * 255).toInt().coerceIn(0, 255)
    val b = (bf * 255).toInt().coerceIn(0, 255)

    return Color(r, g, b)
}

val svDrawer: (Canvas, Size) -> Unit = { canvas, size ->
    val fullHueColor = Color.hsv(360f, 1f, 1f)
    canvas.drawRect(
        paint = Paint().apply { color = Color.White },
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

val hvDrawer: (Canvas, Size) -> Unit = { canvas, size ->
    canvas.drawRect(
        paint = Paint().apply { color = Color.White },
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
}

val hsDrawer: (Canvas, Size) -> Unit = { canvas, size ->
    canvas.drawRect(
        paint = Paint().apply { color = Color.White },
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
}

val hsCircleDrawer: (Canvas, Size) -> Unit = { canvas, size ->
    canvas.drawCircle(
        center = size.center,
        paint = Paint().apply { color = Color.White },
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

    // TODO: Add darkening
//    canvas.drawCircle(
//        center = size.center,
//        paint = Paint().apply {
//            blendMode = BlendMode.Modulate
//            color = Color(0xFF777777)
//        },
//        radius = size.width / 2f,
//    )
}

val hvCircleDrawer: (Canvas, Size) -> Unit = { canvas, size ->
    canvas.drawCircle(
        center = size.center,
        paint = Paint().apply { color = Color.White },
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

    // TODO: Add darkening
//    canvas.drawCircle(
//        center = size.center,
//        paint = Paint().apply {
//            blendMode = BlendMode.Modulate
//            color = Color(0xFF777777)
//        },
//        radius = size.width / 2f,
//    )
}