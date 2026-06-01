package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.model.LocationSettings
import be.mauricedeke.shinkai.domain.model.NotificationSettings
import be.mauricedeke.shinkai.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {
    private var notifSettings = FakeDataSource.defaultNotificationSettings
    private var locationSettings = FakeDataSource.defaultLocationSettings

    override suspend fun getNotificationSettings(): NotificationSettings = notifSettings
    override suspend fun updateNotificationSettings(settings: NotificationSettings) { notifSettings = settings }
    override suspend fun getLocationSettings(): LocationSettings = locationSettings
    override suspend fun updateLocationSettings(settings: LocationSettings) { locationSettings = settings }
}
