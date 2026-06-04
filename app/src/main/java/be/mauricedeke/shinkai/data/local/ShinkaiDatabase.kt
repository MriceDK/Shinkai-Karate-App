package be.mauricedeke.shinkai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(
    entities = [BeltNoteEntity::class, NotificationSettingsEntity::class, LocationSettingsEntity::class, UserProfileEntity::class, StrengthResultEntity::class, ShortcutsEntity::class],
    version = 12,
    exportSchema = false
)
abstract class ShinkaiDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun notificationSettingsDao(): NotificationSettingsDao
    abstract fun locationSettingsDao(): LocationSettingsDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun strengthResultDao(): StrengthResultDao
    abstract fun shortcutsDao(): ShortcutsDao
}
