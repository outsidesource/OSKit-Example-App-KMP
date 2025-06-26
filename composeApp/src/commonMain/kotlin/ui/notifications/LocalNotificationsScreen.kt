package ui.notifications

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import ui.common.Screen

@Composable
fun LocalNotificationsScreen(
    interactor: LocalNotificationsScreenViewInteractor = rememberInjectForRoute(),
) {
    val state = interactor.collectAsState()

    Screen("Local Notifications") {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.pendingTitle,
            placeholder = { Text("Title") },
            onValueChange = interactor::onTitleChanged
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.pendingMessage,
            placeholder = { Text("Message") },
            onValueChange = interactor::onMessageChanged
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = interactor::showNotification
        ) {
            Text("Show Notification")
        }
    }
}