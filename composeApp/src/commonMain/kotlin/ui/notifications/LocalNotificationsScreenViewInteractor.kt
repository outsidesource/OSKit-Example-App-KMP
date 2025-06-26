package ui.notifications

import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.notifications.KmpLocalNotifications
import com.outsidesource.oskitkmp.notifications.NotificationAction
import com.outsidesource.oskitkmp.notifications.NotificationData
import kotlinx.coroutines.launch

data class LocalNotificationsScreenState(
    val pendingTitle: String = "",
    val pendingMessage: String = "",
)

class LocalNotificationsScreenViewInteractor(
    private val notificationsService: KmpLocalNotifications
) : Interactor<LocalNotificationsScreenState>(
    initialState = LocalNotificationsScreenState(),
) {

    fun showNotification() {
        interactorScope.launch {

            val data = NotificationData(
                id = "1234",
                title = state.pendingTitle,
                message = state.pendingMessage,
                actions = listOf(
                    NotificationAction(id = "1234", "open")
                )

            )
            notificationsService.notificationManager.showNotification(data)
        }
    }

    fun onTitleChanged(value: String) {
        update { state -> state.copy(pendingTitle = value) }
    }

    fun onMessageChanged(value: String) {
        update { state -> state.copy(pendingMessage = value) }
    }
}