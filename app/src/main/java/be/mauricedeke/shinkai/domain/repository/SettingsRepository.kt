package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.LocationSettings
import be.mauricedeke.shinkai.domain.model.NotificationSettings

interface SettingsRepository {
    suspend fun getNotificationSettings(): NotificationSettings
    suspend fun updateNotificationSettings(settings: NotificationSettings)
    suspend fun getLocationSettings(): LocationSettings
    suspend fun updateLocationSettings(settings: LocationSettings)
}
