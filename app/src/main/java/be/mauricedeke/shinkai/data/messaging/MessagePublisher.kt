package be.mauricedeke.shinkai.data.messaging

import be.mauricedeke.shinkai.domain.model.NotificationMessage

interface MessagePublisher {
    suspend fun publish(message: NotificationMessage, userId: String)
}
