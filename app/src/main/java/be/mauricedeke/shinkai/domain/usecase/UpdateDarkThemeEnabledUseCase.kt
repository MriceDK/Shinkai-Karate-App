package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.ThemeRepository
import javax.inject.Inject

class UpdateDarkThemeEnabledUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    suspend operator fun invoke(enabled: Boolean) = themeRepository.updateDarkThemeEnabled(enabled)
}

