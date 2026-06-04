package be.mauricedeke.shinkai.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "shinkai_settings")

class ThemeDataStore @Inject constructor(private val context: Context) {

    private val darkThemeKey = booleanPreferencesKey("dark_theme_enabled")

    val darkThemeEnabled: Flow<Boolean?> = context.dataStore.data.map { prefs ->
        prefs[darkThemeKey]
    }

    suspend fun setDarkThemeEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[darkThemeKey] = enabled }
    }
}
