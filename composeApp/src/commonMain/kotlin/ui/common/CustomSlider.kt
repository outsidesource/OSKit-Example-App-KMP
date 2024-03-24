package ui.common

import androidx.compose.material.Slider
import androidx.compose.runtime.*

@Composable
fun CustomSlider(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    var localValue by remember { mutableStateOf(value) }
    var useLocalValue by remember { mutableStateOf(false) }

    Slider(
        value = if (useLocalValue) localValue else value,
        valueRange = valueRange,
        onValueChange = {
            useLocalValue = true
            localValue = it
            onValueChange(it)
        },
        onValueChangeFinished = { useLocalValue = false }
    )
}