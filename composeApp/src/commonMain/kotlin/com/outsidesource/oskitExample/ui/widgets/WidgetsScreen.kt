package com.outsidesource.oskitExample.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outsidesource.oskitExample.ui.app.theme.AppTheme
import com.outsidesource.oskitExample.ui.common.Screen
import com.outsidesource.oskitcompose.form.KMPWheelPicker
import com.outsidesource.oskitcompose.form.rememberKmpWheelPickerState

@Composable
fun WidgetsScreen() {
    Screen("Widgets") {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WidgetLabel("Wheel Picker")
            KMPWheelPicker(
                modifier = Modifier.width(150.dp).height(170.dp),
                items = (0..100).toList(),
                selectedIndex = 0,
                state = rememberKmpWheelPickerState(isInfinite = true, initiallySelectedItemIndex = 0),
                onChange = {},
            ) {
                Box(modifier = Modifier.height(34.dp), contentAlignment = Alignment.Center) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun WidgetLabel(
    text: String
) {
    Text(text = text, style = AppTheme.typography.label)
}