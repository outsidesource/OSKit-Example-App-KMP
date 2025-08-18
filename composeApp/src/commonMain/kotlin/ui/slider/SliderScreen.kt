package ui.slider

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.form.*
import com.outsidesource.oskitcompose.popup.ModalStyles
import com.outsidesource.oskitkmp.text.KmpNumberFormatter
import ui.app.theme.AppTheme
import ui.common.Screen

@Composable
fun SliderScreen() {
    val colors = AppTheme.colors
    val defaultTickStyle = remember(colors) {
        KmpSliderTickStyle().let {
            it.copy(
                labelTextStyle = it.labelTextStyle.copy(color = colors.fontColor),
                shapeBrush = SolidColor(colors.fontColor),
            )
        }
    }

    Screen("Sliders") {
        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState())
                .widthIn(max = 600.dp)
                .systemBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            val testValues = remember { mutableStateOf(mapOf("0" to -20f, "1" to 0f, "2" to 20f)) }
            var isDisabled by remember { mutableStateOf(false) }

            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Disabled")
                Checkbox(checked = isDisabled, onCheckedChange = { isDisabled = it })
            }

            KmpSlider(
                label = "Group",
                isEnabled = !isDisabled,
                multiThumbMode = MultiThumbMode.Group,
                values = testValues.value,
                range = -100f..100f,
                styles = AppTheme.colors.sliderStyles,
                manualEntrySlot = { isVisible, valueString, onTextChange, onCancel, onCommit ->
                    AppManualEntryModal(isVisible, valueString, onTextChange, onCancel, onCommit)
                },
                ticks = remember(colors) {
                    (-100..100 step 10).map {
                        KmpSliderTick(
                            value = it.toFloat(),
                            label = if (it % 20 == 0) it.toString() else null,
                            style = (if (it % 20 == 0) KmpSliderTickStyle.Line.Medium else KmpSliderTickStyle.Line.Short)
                                .copy(
                                    labelTextStyle = defaultTickStyle.labelTextStyle,
                                    shapeBrush = defaultTickStyle.shapeBrush,
                                ),
                        )
                    }
                },
                onChange = {
                    testValues.value = testValues.value.toMutableMap().apply {
                        it.forEach { (key, value) -> this[key] = value }
                    }
                },
            )

            KmpSlider(
                values = remember { derivedStateOf { mapOf("0" to (testValues.value["0"] ?: 0f)) } }.value,
                label = "Start Alignment",
                isEnabled = !isDisabled,
                units = "%",
                range = -100f..100f,
                styles = AppTheme.colors.sliderStyles,
                manualEntrySlot = { isVisible, valueString, onTextChange, onCancel, onCommit ->
                    AppManualEntryModal(isVisible, valueString, onTextChange, onCancel, onCommit)
                },
                ticks = remember(colors) {
                    (-100..100 step 20).map {
                        KmpSliderTick(
                            value = it.toFloat(),
                            label = it.toString(),
                            style = defaultTickStyle,
                        )
                    }
                },
                onChange = {
                    testValues.value = testValues.value.toMutableMap().apply {
                        it.forEach { (key, value) -> this[key] = value }
                    }
                },
            )

            KmpSlider(
                values = remember { derivedStateOf { mapOf("1" to (testValues.value["1"] ?: 0f)) } }.value,
                label = "Center Alignment",
                isEnabled = !isDisabled,
                units = "%",
                range = -100f..100f,
                manualEntrySlot = { isVisible, valueString, onTextChange, onCancel, onCommit ->
                    AppManualEntryModal(isVisible, valueString, onTextChange, onCancel, onCommit)
                },
                styles = AppTheme.colors.sliderStyles.copy(
                    trackFill = Brush.linearGradient(
                        0f to Color.Blue,
                        1f to Color.Red,
                    ),
                    trackFillAlignment = KmpSliderAlignment.Center,
                ),
                ticks = remember(colors) {
                    (-100..100 step 20).map {
                        KmpSliderTick(
                            value = it.toFloat(),
                            label = it.toString(),
                            style = KmpSliderTickStyle.Line.Medium.copy(
                                labelTextStyle = defaultTickStyle.labelTextStyle,
                                shapeBrush = defaultTickStyle.shapeBrush,
                            ),
                        )
                    }
                },
                onChange = {
                    testValues.value = testValues.value.toMutableMap().apply {
                        it.forEach { (key, value) -> this[key] = value }
                    }
                },
            )

            KmpSlider(
                values = remember { derivedStateOf { mapOf("2" to (testValues.value["2"] ?: 0f)) } }.value,
                label = "End Alignment",
                isEnabled = !isDisabled,
                units = "%",
                range = -100f..100f,
                manualEntrySlot = { isVisible, valueString, onTextChange, onCancel, onCommit ->
                    AppManualEntryModal(isVisible, valueString, onTextChange, onCancel, onCommit)
                },
                styles = AppTheme.colors.sliderStyles.copy(
                    trackFill = Brush.linearGradient(
                        0f to Color.Blue,
                        1f to Color.LightGray,
                    ),
                    trackFillAlignment = KmpSliderAlignment.End,
                ),
                ticks = remember(colors) {
                    (-100..100 step 20).map {
                        KmpSliderTick(
                            value = it.toFloat(),
                            label = it.toString(),
                            style = KmpSliderTickStyle.Line.Medium.copy(
                                labelTextStyle = defaultTickStyle.labelTextStyle,
                                shapeBrush = defaultTickStyle.shapeBrush,
                            ),
                        )
                    }
                },
                onChange = {
                    testValues.value = testValues.value.toMutableMap().apply {
                        it.forEach { (key, value) -> this[key] = value }
                    }
                },
            )

            KmpSlider(
                values = remember {
                    derivedStateOf {
                        mapOf(
                            "0" to (testValues.value["0"] ?: 0f),
                            "2" to (testValues.value["2"] ?: 0f)
                        )
                    }
                }.value,
                label = "Range",
                isEnabled = !isDisabled,
                range = -100f..100f,
                styles = AppTheme.colors.sliderStyles,
                manualEntrySlot = { isVisible, valueString, onTextChange, onCancel, onCommit ->
                    AppManualEntryModal(isVisible, valueString, onTextChange, onCancel, onCommit)
                },
                deadband = 1f,
                ticks = remember(colors) {
                    buildList {
                        KmpSliderTick(
                            value = -100f,
                            label = "0",
                            style = defaultTickStyle.copy(
                                labelPosition = KmpSliderTickPosition(alignment = KmpSliderAlignment.End, offset = 4.dp),
                                shapeSize = DpAxisSize(0.dp, 0.dp),
                            ),
                        ).let { add(it) }
                        KmpSliderTick(
                            value = 100f,
                            label = "100",
                            style = defaultTickStyle.copy(
                                labelPosition = KmpSliderTickPosition(alignment = KmpSliderAlignment.End, offset = 4.dp),
                                shapeSize = DpAxisSize(0.dp, 0.dp),
                            ),
                        ).let { add(it) }
                        for (i in -90..90 step 10) {
                            KmpSliderTick(
                                value = i.toFloat(),
                                style = KmpSliderTickStyle.Circle.copy(
                                    shapeBrush = SolidColor(Color.White.copy(alpha = .75f)),
                                    shapePosition = KmpSliderTickPosition(alignment = KmpSliderAlignment.Center),
                                    labelTextStyle = defaultTickStyle.labelTextStyle,
                                ),
                            ).let { add(it) }
                        }
                    }
                },
                onChange = {
                    testValues.value = testValues.value.toMutableMap().apply {
                        it.forEach { (key, value) -> this[key] = value }
                    }
                },
            )

            val logValues = remember { mutableStateOf(mapOf("0" to 50f)) }
            KmpSlider(
                values = logValues.value,
                isEnabled = !isDisabled,
                label = "Logarithmic",
                range = 20f..20_000f,
                valueFormatter = remember {
                    val numberFormatter = KmpNumberFormatter(maximumFractionDigits = 0)
                    return@remember { numberFormatter.format(it) }
                },
                units = "hz",
                logarithmic = true,
                styles = AppTheme.colors.sliderStyles,
                manualEntrySlot = { isVisible, valueString, onTextChange, onCancel, onCommit ->
                    AppManualEntryModal(isVisible, valueString, onTextChange, onCancel, onCommit)
                },
                ticks = remember(colors) {
                    buildList {
                        add(KmpSliderTick(value = 20f, label = "20hz", style = defaultTickStyle))
                        add(KmpSliderTick(value = 50f, label = "50", style = defaultTickStyle))
                        add(KmpSliderTick(value = 100f, label = "100", style = defaultTickStyle))
                        add(KmpSliderTick(value = 200f, label = "200", style = defaultTickStyle))
                        add(KmpSliderTick(value = 500f, label = "500", style = defaultTickStyle))
                        add(KmpSliderTick(value = 1_000f, label = "1k", style = defaultTickStyle))
                        add(KmpSliderTick(value = 2_000f, label = "2k", style = defaultTickStyle))
                        add(KmpSliderTick(value = 5_000f, label = "5k", style = defaultTickStyle))
                        add(KmpSliderTick(value = 10_000f, label = "10k", style = defaultTickStyle))
                        add(KmpSliderTick(value = 20_000f, label = "20k", style = defaultTickStyle))
                    }
                },
                onChange = remember {
                    {
                        logValues.value = logValues.value.toMutableMap().apply {
                            it.forEach { (key, value) -> this[key] = value }
                        }
                    }
                },
            )

            KmpSlider(
                modifier = Modifier
                    .height(300.dp),
                styles = AppTheme.colors.sliderStyles,
                values = testValues.value,
                isEnabled = !isDisabled,
                direction = KmpSliderDirection.Vertical,
                multiThumbMode = MultiThumbMode.Group,
                label = "Basic".uppercase(),
                units = "%",
                range = -100f..100f,
                manualEntrySlot = { isVisible, valueString, onTextChange, onCancel, onCommit ->
                    AppManualEntryModal(isVisible, valueString, onTextChange, onCancel, onCommit)
                },
                ticks = remember(colors) {
                    (-100..100 step 20).map {
                        KmpSliderTick(
                            value = it.toFloat(),
                            label = it.toString(),
                            style = defaultTickStyle,
                        )
                    }
                },
                valueLabelSlot = {
                    ValueLabel(modifier = Modifier.width(110.dp))
                },
                onChange = {
                    testValues.value = testValues.value.toMutableMap().apply {
                        it.forEach { (key, value) -> this[key] = value }
                    }
                },
            )
        }
    }
}

@Composable
private fun KmpSliderScope.AppManualEntryModal(
    isVisible: Boolean,
    valueString: String,
    onTextChange: (String) -> Unit,
    onCancel: () -> Unit,
    onCommit: () -> Unit,
) {
    MaterialTheme(
        typography = MaterialTheme.typography.copy(
            button = MaterialTheme.typography.button.copy(
                color = AppTheme.colors.fontColor,
            )
        )
    ) {
        ManualEntryModal(
            isVisible = isVisible,
            valueString = valueString,
            onTextChange = onTextChange,
            onCancel = onCancel,
            onCommit = onCommit,
            styles = ModalStyles(
                backgroundColor = AppTheme.colors.screenBackgroundColor,
            )
        )
    }
}