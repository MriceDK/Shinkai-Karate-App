package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.ThemeRepository
import javax.inject.Inject

class GetDarkThemeEnabledUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    suspend operator fun invoke(): Boolean? = themeRepository.getDarkThemeEnabled()
}

