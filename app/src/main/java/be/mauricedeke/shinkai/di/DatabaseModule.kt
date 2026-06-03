package be.mauricedeke.shinkai.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import be.mauricedeke.shinkai.data.local.LocationSettingsDao
import be.mauricedeke.shinkai.data.local.NoteDao
import be.mauricedeke.shinkai.data.local.NotificationSettingsDao
import be.mauricedeke.shinkai.data.local.ShinkaiDatabase
import be.mauricedeke.shinkai.data.local.ShortcutsDao
import be.mauricedeke.shinkai.data.local.StrengthResultDao
import be.mauricedeke.shinkai.data.local.ThemePreferenceDao
import be.mauricedeke.shinkai.data.local.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `theme_preferences` (
                `id` INTEGER NOT NULL,
                `darkThemeEnabled` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `notification_settings` (
                `id` INTEGER NOT NULL,
                `eventNotifications` INTEGER NOT NULL,
                `trainingNotifications` INTEGER NOT NULL,
                `changeNotifications` INTEGER NOT NULL,
                `examNotifications` INTEGER NOT NULL,
                `updateNotifications` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
    }
}

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `location_settings` (
                `id` INTEGER NOT NULL,
                `useForTrainingLocations` INTEGER NOT NULL,
                `useForImprovements` INTEGER NOT NULL,
                `useForTrackingTrainings` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
    }
}

private val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `user_profile` (
                `id` INTEGER NOT NULL,
                `name` TEXT NOT NULL,
                `email` TEXT NOT NULL,
                `belt` TEXT NOT NULL,
                `profilePictureUri` TEXT,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
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
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `shortcuts` (
                `id` INTEGER NOT NULL,
                `shortcuts` TEXT NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
    }
}

private val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `strength_results` (
                `type` TEXT NOT NULL,
                `bestScore` INTEGER NOT NULL,
                PRIMARY KEY(`type`)
            )
            """.trimIndent()
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShinkaiDatabase =
        Room.databaseBuilder(context, ShinkaiDatabase::class.java, "shinkai_db")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10)
            .build()

    @Provides
    fun provideNoteDao(db: ShinkaiDatabase): NoteDao = db.noteDao()

    @Provides
    fun provideThemePreferenceDao(db: ShinkaiDatabase): ThemePreferenceDao = db.themePreferenceDao()

    @Provides
    fun provideNotificationSettingsDao(db: ShinkaiDatabase): NotificationSettingsDao = db.notificationSettingsDao()

    @Provides
    fun provideLocationSettingsDao(db: ShinkaiDatabase): LocationSettingsDao = db.locationSettingsDao()

    @Provides
    fun provideUserProfileDao(db: ShinkaiDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    fun provideStrengthResultDao(db: ShinkaiDatabase): StrengthResultDao = db.strengthResultDao()

    @Provides
    fun provideShortcutsDao(db: ShinkaiDatabase): ShortcutsDao = db.shortcutsDao()
}
