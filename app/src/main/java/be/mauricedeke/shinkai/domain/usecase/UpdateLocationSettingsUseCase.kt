package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.LocationSettings
import be.mauricedeke.shinkai.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLocationSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(settings: LocationSettings) =
        settingsRepository.updateLocationSettings(settings)
}
