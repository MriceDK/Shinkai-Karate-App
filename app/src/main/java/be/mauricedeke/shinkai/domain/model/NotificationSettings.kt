package be.mauricedeke.shinkai.domain.model

data class NotificationSettings(
    val eventNotifications: Boolean = true,
    val trainingNotifications: Boolean = true,
    val changeNotifications: Boolean = true,
    val examNotifications: Boolean = false,
    val updateNotifications: Boolean = true
)
