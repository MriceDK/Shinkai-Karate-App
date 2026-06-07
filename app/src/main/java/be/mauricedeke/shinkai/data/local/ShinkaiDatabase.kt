package be.mauricedeke.shinkai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import be.mauricedeke.shinkai.data.local.room.dao.BeltDao
import be.mauricedeke.shinkai.data.local.room.dao.KataDao
import be.mauricedeke.shinkai.data.local.room.dao.LexiconDao
import be.mauricedeke.shinkai.data.local.room.dao.LocationSettingsDao
import be.mauricedeke.shinkai.data.local.room.dao.NoteDao
import be.mauricedeke.shinkai.data.local.room.dao.NotificationSettingsDao
import be.mauricedeke.shinkai.data.local.room.dao.ShortcutsDao
import be.mauricedeke.shinkai.data.local.room.dao.StrengthResultDao
import be.mauricedeke.shinkai.data.local.room.dao.TechniekDao
import be.mauricedeke.shinkai.data.local.room.dao.UserProfileDao
import be.mauricedeke.shinkai.data.local.room.entity.BeltEntity
import be.mauricedeke.shinkai.data.local.room.entity.BeltNoteEntity
import be.mauricedeke.shinkai.data.local.room.entity.KataEntity
import be.mauricedeke.shinkai.data.local.room.entity.LexiconEntryEntity
import be.mauricedeke.shinkai.data.local.room.entity.LocationSettingsEntity
import be.mauricedeke.shinkai.data.local.room.entity.NotificationSettingsEntity
import be.mauricedeke.shinkai.data.local.room.entity.ShortcutsEntity
import be.mauricedeke.shinkai.data.local.room.entity.StrengthResultEntity
import be.mauricedeke.shinkai.data.local.room.entity.TechniekEntity
import be.mauricedeke.shinkai.data.local.room.entity.UserProfileEntity

@TypeConverters(Converters::class)
@Database(
    entities = [
        BeltNoteEntity::class,
        NotificationSettingsEntity::class,
        LocationSettingsEntity::class,
        UserProfileEntity::class,
        StrengthResultEntity::class,
        ShortcutsEntity::class,
        BeltEntity::class,
        TechniekEntity::class,
        LexiconEntryEntity::class,
        KataEntity::class
    ],
    version = 14,
    exportSchema = false
)
abstract class ShinkaiDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun notificationSettingsDao(): NotificationSettingsDao
    abstract fun locationSettingsDao(): LocationSettingsDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun strengthResultDao(): StrengthResultDao
    abstract fun shortcutsDao(): ShortcutsDao
    abstract fun beltDao(): BeltDao
    abstract fun techniekDao(): TechniekDao
    abstract fun lexiconDao(): LexiconDao
    abstract fun kataDao(): KataDao
}
