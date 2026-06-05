package be.mauricedeke.shinkai.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_settings")
data class NotificationSettingsEntity(
    @PrimaryKey val id: Int = 0,
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
