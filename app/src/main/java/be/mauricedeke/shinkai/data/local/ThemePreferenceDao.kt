package be.mauricedeke.shinkai.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ThemePreferenceDao {
    @Query("SELECT * FROM theme_preferences WHERE id = 0")
    suspend fun getThemePreference(): ThemePreferenceEntity?

    @Upsert
    suspend fun upsertThemePreference(entity: ThemePreferenceEntity)
}

