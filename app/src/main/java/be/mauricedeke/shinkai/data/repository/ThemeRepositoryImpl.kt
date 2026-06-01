package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.ThemePreferenceDao
import be.mauricedeke.shinkai.data.local.ThemePreferenceEntity
import be.mauricedeke.shinkai.domain.repository.ThemeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    private val themePreferenceDao: ThemePreferenceDao
) : ThemeRepository {
    override suspend fun getDarkThemeEnabled(): Boolean? =
        themePreferenceDao.getThemePreference()?.darkThemeEnabled

    override suspend fun updateDarkThemeEnabled(enabled: Boolean) {
        themePreferenceDao.upsertThemePreference(
            ThemePreferenceEntity(darkThemeEnabled = enabled)
        )
    }
}

