package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outsidesource.oskitcompose.form.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ui.app.theme.AppTheme
import ui.common.Screen

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
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { isVisible = !isVisible }
                        .background(Color(0x1A000000))
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    text = "$date",
                    lineHeight = 24.sp,
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
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { isVisible = !isVisible }
                        .background(Color(0x1A000000))
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    text = "$time",
                    lineHeight = 24.sp,
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