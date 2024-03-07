package com.outsidesource.oskitExample.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outsidesource.oskitExample.ui.app.theme.AppTheme
import com.outsidesource.oskitExample.ui.common.Screen
import com.outsidesource.oskitcompose.form.*
import com.outsidesource.oskitcompose.modifier.defaultMaxSize
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WidgetsScreen() {
    Screen("Widgets") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column {
                WidgetLabel("Wheel Picker")
                KMPWheelPicker(
                    modifier = Modifier.width(150.dp).height(170.dp),
                    items = (0..10).toList(),
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
            Column {
                var isVisible by remember { mutableStateOf(false) }
                var date by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) }

                WidgetLabel("Date Picker")
                TextField(
                    modifier = Modifier.clickable { isVisible = !isVisible },
                    value = "$date",
                    enabled = false,
                    onValueChange = {}
                )
                KMPDatePickerModal(
                    isVisible = isVisible,
                    date = date,
                    onChange = { date = it },
                    onDismissRequest = { isVisible = false },
                    datePickerStyles = rememberKmpDatePickerStyles().copy(buttonStyle = TextStyle(color = AppTheme.colors.fontColor))
                )
            }
            Column {
                var isVisible by remember { mutableStateOf(false) }
                var time by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time) }

                WidgetLabel("Time Picker")
                TextField(
                    modifier = Modifier.clickable { isVisible = !isVisible },
                    value = "$time",
                    enabled = false,
                    onValueChange = {}
                )

                KMPTimePickerModal(
                    isVisible = isVisible,
                    time = time,
                    minuteStep = 1,
                    onChange = { time = it },
                    onDismissRequest = { isVisible = false },
                    timePickerStyles = rememberKmpTimePickerStyles().copy(buttonStyle = TextStyle(color = AppTheme.colors.fontColor))
                )
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