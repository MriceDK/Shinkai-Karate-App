package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.room.dao.LocationSettingsDao
import be.mauricedeke.shinkai.data.local.room.entity.LocationSettingsEntity
import be.mauricedeke.shinkai.data.local.room.dao.NotificationSettingsDao
import be.mauricedeke.shinkai.data.local.room.entity.NotificationSettingsEntity
import be.mauricedeke.shinkai.data.local.room.dao.ShortcutsDao
import be.mauricedeke.shinkai.data.local.room.entity.ShortcutsEntity
import be.mauricedeke.shinkai.domain.model.LocationSettings
import be.mauricedeke.shinkai.domain.model.NotificationSettings
import be.mauricedeke.shinkai.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val notificationSettingsDao: NotificationSettingsDao,
    private val locationSettingsDao: LocationSettingsDao,
    private val shortcutsDao: ShortcutsDao
) : SettingsRepository {

    override suspend fun getNotificationSettings(): NotificationSettings {
        val entity = notificationSettingsDao.get() ?: return NotificationSettings()
        return NotificationSettings(
            eventNotifications = entity.eventNotifications,
            eventReminderEnabled = entity.eventReminderEnabled,
            reminderMinutesBefore = entity.reminderMinutesBefore,
            trainingNotifications = entity.trainingNotifications,
            trainingReminderEnabled = entity.trainingReminderEnabled,
            trainingReminderMinutesBefore = entity.trainingReminderMinutesBefore,
            changeNotifications = entity.changeNotifications,
            examNotifications = entity.examNotifications,
            examReminderEnabled = entity.examReminderEnabled,
            examReminderMinutesBefore = entity.examReminderMinutesBefore,
            updateNotifications = entity.updateNotifications,
        )
    }

    override suspend fun updateNotificationSettings(settings: NotificationSettings) {
        notificationSettingsDao.upsert(
            NotificationSettingsEntity(
                eventNotifications = settings.eventNotifications,
                eventReminderEnabled = settings.eventReminderEnabled,
                reminderMinutesBefore = settings.reminderMinutesBefore,
                trainingNotifications = settings.trainingNotifications,
                trainingReminderEnabled = settings.trainingReminderEnabled,
                trainingReminderMinutesBefore = settings.trainingReminderMinutesBefore,
                changeNotifications = settings.changeNotifications,
                examNotifications = settings.examNotifications,
                examReminderEnabled = settings.examReminderEnabled,
                examReminderMinutesBefore = settings.examReminderMinutesBefore,
                updateNotifications = settings.updateNotifications,
            )
        )
    }

    override suspend fun getLocationSettings(): LocationSettings {
        val entity = locationSettingsDao.get() ?: return LocationSettings()
        return LocationSettings(
            showOnMap = entity.showOnMap,
            useForGeofencing = entity.useForGeofencing,
            useForStatistics = entity.useForStatistics
        )
    }

    override suspend fun updateLocationSettings(settings: LocationSettings) {
        locationSettingsDao.upsert(
            LocationSettingsEntity(
                showOnMap = settings.showOnMap,
                useForGeofencing = settings.useForGeofencing,
                useForStatistics = settings.useForStatistics
            )
        )
    }

    override suspend fun getShortcuts(): List<String> {
        val entity = shortcutsDao.get() ?: return listOf("EVENTS", "KAART", "LEXICON", "TECHNIEKEN")
        return entity.shortcuts.split(",").filter { it.isNotBlank() }
    }

    override suspend fun updateShortcuts(ids: List<String>) {
        shortcutsDao.upsert(ShortcutsEntity(shortcuts = ids.joinToString(",")))
    }
}
