package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.SettingsRepository
import javax.inject.Inject

class GetShortcutsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): List<String> = settingsRepository.getShortcuts()
}
