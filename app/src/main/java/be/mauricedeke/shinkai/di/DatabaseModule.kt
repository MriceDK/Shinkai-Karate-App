package be.mauricedeke.shinkai.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import be.mauricedeke.shinkai.data.local.ShinkaiDatabase
import be.mauricedeke.shinkai.data.local.room.dao.BeltDao
import be.mauricedeke.shinkai.data.local.room.dao.LexiconDao
import be.mauricedeke.shinkai.data.local.room.dao.LocationSettingsDao
import be.mauricedeke.shinkai.data.local.room.dao.NoteDao
import be.mauricedeke.shinkai.data.local.room.dao.NotificationSettingsDao
import be.mauricedeke.shinkai.data.local.room.dao.ShortcutsDao
import be.mauricedeke.shinkai.data.local.room.dao.StrengthResultDao
import be.mauricedeke.shinkai.data.local.room.dao.TechniekDao
import be.mauricedeke.shinkai.data.local.room.dao.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `theme_preferences` (`id` INTEGER NOT NULL, `darkThemeEnabled` INTEGER NOT NULL, PRIMARY KEY(`id`))")
    }
}
private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `notification_settings` (`id` INTEGER NOT NULL, `eventNotifications` INTEGER NOT NULL, `trainingNotifications` INTEGER NOT NULL, `changeNotifications` INTEGER NOT NULL, `examNotifications` INTEGER NOT NULL, `updateNotifications` INTEGER NOT NULL, PRIMARY KEY(`id`))")
    }
}
private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `location_settings` (`id` INTEGER NOT NULL, `useForTrainingLocations` INTEGER NOT NULL, `useForImprovements` INTEGER NOT NULL, `useForTrackingTrainings` INTEGER NOT NULL, PRIMARY KEY(`id`))")
    }
}
private val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `email` TEXT NOT NULL, `belt` TEXT NOT NULL, `profilePictureUri` TEXT, PRIMARY KEY(`id`))")
    }
}
private val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `strength_results` (`type` TEXT NOT NULL, `bestScore` INTEGER NOT NULL, PRIMARY KEY(`type`))")
    }
}
private val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `notification_settings` ADD COLUMN `reminderMinutesBefore` INTEGER NOT NULL DEFAULT 30")
    }
}
private val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `notification_settings` ADD COLUMN `trainingReminderMinutesBefore` INTEGER NOT NULL DEFAULT 30")
        db.execSQL("ALTER TABLE `notification_settings` ADD COLUMN `examReminderMinutesBefore` INTEGER NOT NULL DEFAULT 1440")
    }
}
private val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `user_profile` ADD COLUMN `userId` TEXT")
    }
}
private val MIGRATION_9_10 = object : Migration(9, 10) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `shortcuts` (`id` INTEGER NOT NULL, `shortcuts` TEXT NOT NULL, PRIMARY KEY(`id`))")
    }
}
private val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `notification_settings` ADD COLUMN `eventReminderEnabled` INTEGER NOT NULL DEFAULT 1")
        db.execSQL("ALTER TABLE `notification_settings` ADD COLUMN `trainingReminderEnabled` INTEGER NOT NULL DEFAULT 1")
        db.execSQL("ALTER TABLE `notification_settings` ADD COLUMN `examReminderEnabled` INTEGER NOT NULL DEFAULT 1")
    }
}
private val MIGRATION_11_12 = object : Migration(11, 12) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS `theme_preferences`")
    }
}
private val MIGRATION_12_13 = object : Migration(12, 13) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `belts` (`name` TEXT NOT NULL, `beltColor` TEXT NOT NULL, `programmeJson` TEXT NOT NULL, PRIMARY KEY(`name`))")
        db.execSQL("CREATE TABLE IF NOT EXISTS `technieken` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `beltName` TEXT NOT NULL, `description` TEXT NOT NULL, `programma` TEXT NOT NULL, PRIMARY KEY(`id`))")
        db.execSQL("CREATE TABLE IF NOT EXISTS `lexicon_entries` (`id` TEXT NOT NULL, `japaneseWord` TEXT NOT NULL, `translation` TEXT NOT NULL, `description` TEXT NOT NULL, PRIMARY KEY(`id`))")
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShinkaiDatabase =
        Room.databaseBuilder(context, ShinkaiDatabase::class.java, "shinkai_db")
            .addMigrations(
                MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6,
                MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11,
                MIGRATION_11_12, MIGRATION_12_13
            )
            .build()

    @Provides fun provideNoteDao(db: ShinkaiDatabase): NoteDao = db.noteDao()
    @Provides fun provideNotificationSettingsDao(db: ShinkaiDatabase): NotificationSettingsDao = db.notificationSettingsDao()
    @Provides fun provideLocationSettingsDao(db: ShinkaiDatabase): LocationSettingsDao = db.locationSettingsDao()
    @Provides fun provideUserProfileDao(db: ShinkaiDatabase): UserProfileDao = db.userProfileDao()
    @Provides fun provideStrengthResultDao(db: ShinkaiDatabase): StrengthResultDao = db.strengthResultDao()
    @Provides fun provideShortcutsDao(db: ShinkaiDatabase): ShortcutsDao = db.shortcutsDao()
    @Provides fun provideBeltDao(db: ShinkaiDatabase): BeltDao = db.beltDao()
    @Provides fun provideTechniekDao(db: ShinkaiDatabase): TechniekDao = db.techniekDao()
    @Provides fun provideLexiconDao(db: ShinkaiDatabase): LexiconDao = db.lexiconDao()
}
