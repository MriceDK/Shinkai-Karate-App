package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.LocationSettingsDao
import be.mauricedeke.shinkai.data.local.LocationSettingsEntity
import be.mauricedeke.shinkai.data.local.NotificationSettingsDao
import be.mauricedeke.shinkai.data.local.NotificationSettingsEntity
import be.mauricedeke.shinkai.data.local.ShortcutsDao
import be.mauricedeke.shinkai.data.local.ShortcutsEntity
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
            trainingNotifications = entity.trainingNotifications,
            changeNotifications = entity.changeNotifications,
            examNotifications = entity.examNotifications,
            updateNotifications = entity.updateNotifications,
            reminderMinutesBefore = entity.reminderMinutesBefore,
            trainingReminderMinutesBefore = entity.trainingReminderMinutesBefore,
            examReminderMinutesBefore = entity.examReminderMinutesBefore
        )
    }

    override suspend fun updateNotificationSettings(settings: NotificationSettings) {
        notificationSettingsDao.upsert(
            NotificationSettingsEntity(
                eventNotifications = settings.eventNotifications,
                trainingNotifications = settings.trainingNotifications,
                changeNotifications = settings.changeNotifications,
                examNotifications = settings.examNotifications,
                updateNotifications = settings.updateNotifications,
                reminderMinutesBefore = settings.reminderMinutesBefore,
                trainingReminderMinutesBefore = settings.trainingReminderMinutesBefore,
                examReminderMinutesBefore = settings.examReminderMinutesBefore
            )
        )
    }

    override suspend fun getLocationSettings(): LocationSettings {
        val entity = locationSettingsDao.get() ?: return LocationSettings()
        return LocationSettings(
            useForTrainingLocations = entity.useForTrainingLocations,
            useForImprovements = entity.useForImprovements,
            useForTrackingTrainings = entity.useForTrackingTrainings
        )
    }

    override suspend fun updateLocationSettings(settings: LocationSettings) {
        locationSettingsDao.upsert(
            LocationSettingsEntity(
                useForTrainingLocations = settings.useForTrainingLocations,
                useForImprovements = settings.useForImprovements,
                useForTrackingTrainings = settings.useForTrackingTrainings
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
