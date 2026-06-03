package be.mauricedeke.shinkai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BeltNoteEntity::class, ThemePreferenceEntity::class, NotificationSettingsEntity::class, LocationSettingsEntity::class, UserProfileEntity::class],
    version = 5,
    exportSchema = false
)
abstract class ShinkaiDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun themePreferenceDao(): ThemePreferenceDao
    abstract fun notificationSettingsDao(): NotificationSettingsDao
    abstract fun locationSettingsDao(): LocationSettingsDao
    abstract fun userProfileDao(): UserProfileDao
}
