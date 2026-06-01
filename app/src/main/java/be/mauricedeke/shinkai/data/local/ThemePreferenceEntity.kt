package be.mauricedeke.shinkai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "theme_preferences")
data class ThemePreferenceEntity(
    @PrimaryKey val id: Int = 0,
    val darkThemeEnabled: Boolean
)

