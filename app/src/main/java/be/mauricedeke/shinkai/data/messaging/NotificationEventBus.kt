package be.mauricedeke.shinkai.data.messaging

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

data class InAppNotification(val type: String, val title: String, val body: String)

@Singleton
class NotificationEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<InAppNotification>()
    val events: SharedFlow<InAppNotification> = _events.asSharedFlow()

    suspend fun emit(notification: InAppNotification) = _events.emit(notification)
}
