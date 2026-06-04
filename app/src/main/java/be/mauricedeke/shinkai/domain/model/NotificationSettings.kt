package be.mauricedeke.shinkai.domain.model

data class NotificationSettings(
    val eventNotifications: Boolean = true,
    val eventReminderEnabled: Boolean = true,
    val reminderMinutesBefore: Int = 30,
    val trainingNotifications: Boolean = true,
    val trainingReminderEnabled: Boolean = true,
    val trainingReminderMinutesBefore: Int = 30,
    val changeNotifications: Boolean = true,
    val examNotifications: Boolean = false,
    val examReminderEnabled: Boolean = true,
    val examReminderMinutesBefore: Int = 1440,
    val updateNotifications: Boolean = true,
)
