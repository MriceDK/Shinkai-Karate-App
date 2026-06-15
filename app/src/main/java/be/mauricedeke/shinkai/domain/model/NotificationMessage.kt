package be.mauricedeke.shinkai.domain.model

data class NotificationMessage(
    val type: String,
    val title: String,
    val body: String
)
