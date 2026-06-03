package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateShortcutsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(ids: List<String>) = settingsRepository.updateShortcuts(ids)
}
