package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.datastore.ThemeDataStore
import be.mauricedeke.shinkai.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    private val themeDataStore: ThemeDataStore
) : ThemeRepository {
    override suspend fun getDarkThemeEnabled(): Boolean? =
        themeDataStore.darkThemeEnabled.first()

    override suspend fun updateDarkThemeEnabled(enabled: Boolean) {
        themeDataStore.setDarkThemeEnabled(enabled)
    }
}

