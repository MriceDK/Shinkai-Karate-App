package be.mauricedeke.shinkai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_settings")
data class NotificationSettingsEntity(
    @PrimaryKey val id: Int = 0,
    val eventNotifications: Boolean = true,
    val trainingNotifications: Boolean = true,
    val changeNotifications: Boolean = true,
    val examNotifications: Boolean = false,
    val updateNotifications: Boolean = true,
    val reminderMinutesBefore: Int = 30,
    val trainingReminderMinutesBefore: Int = 30,
    val examReminderMinutesBefore: Int = 1440
)
