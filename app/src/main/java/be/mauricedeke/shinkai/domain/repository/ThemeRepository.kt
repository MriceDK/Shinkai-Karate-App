package be.mauricedeke.shinkai.domain.repository

interface ThemeRepository {
    suspend fun getDarkThemeEnabled(): Boolean?
    suspend fun updateDarkThemeEnabled(enabled: Boolean)
}

