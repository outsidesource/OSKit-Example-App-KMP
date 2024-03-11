@file:OptIn(ExperimentalResourceApi::class)

package ui.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import resources.Fonts
import resources.Strings
import ui.app.theme.AppTheme
import ui.common.Screen
import com.outsidesource.oskitcompose.resources.LocalLocaleOverride
import com.outsidesource.oskitcompose.resources.rememberKmpString
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import oskit_example_app_kmp.composeapp.generated.resources.Res
import oskit_example_app_kmp.composeapp.generated.resources.penguin
import oskit_example_app_kmp.composeapp.generated.resources.tux

@Composable
fun ResourcesScreen() {
    Screen("Resources") {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Section(rememberKmpString(Strings.strings)) {
                var time by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time) }
                var localeOverride by remember { mutableStateOf<Locale?>(null) }

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(1_000)
                        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
                    }
                }

                CompositionLocalProvider(LocalLocaleOverride provides localeOverride) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Switch(
                            checked = localeOverride != null,
                            onCheckedChange = {
                                localeOverride = if (localeOverride == null) Locale("es") else null
                            }
                        )
                        Text(rememberKmpString(Strings.language))
                    }
                    Text(
                        text = rememberKmpString(Strings.currentTime, "${time.hour}:${time.minute.toString().padStart(2, '0')}:${time.second.toString().padStart(2, '0')}"),
                    )
                }
            }

            Section(rememberKmpString(Strings.fonts)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = rememberKmpString(Strings.externalFontTest),
                        fontFamily = Fonts.SansSerifPro,
                    )
                    Text(
                        text = rememberKmpString(Strings.standardFontTest),
                        fontFamily = Fonts.Default,
                    )
                }
            }

            Section(rememberKmpString(Strings.images)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    ImageWithCaption(Res.drawable.penguin, rememberKmpString(Strings.pngImage))
                    ImageWithCaption(Res.drawable.tux, rememberKmpString(Strings.vectorDrawableImages))
                }
            }
        }
    }
}

@Composable
private fun Section(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, style = AppTheme.typography.sectionHeader)
        Divider()
        content()
    }
}

@Composable
private fun ImageWithCaption(resource: DrawableResource, caption: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(modifier = Modifier.size(200.dp), painter = painterResource(resource), contentDescription = "")
        Text(caption, fontSize = 12.sp)
    }
}