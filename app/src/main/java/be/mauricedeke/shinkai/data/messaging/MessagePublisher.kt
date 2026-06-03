package be.mauricedeke.shinkai.data.messaging

interface MessagePublisher {
    suspend fun publishMessage(message: String, userId: String)
}