package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.NotificationSettings
import be.mauricedeke.shinkai.domain.repository.SettingsRepository
import javax.inject.Inject

class GetNotificationSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): NotificationSettings = settingsRepository.getNotificationSettings()
}
