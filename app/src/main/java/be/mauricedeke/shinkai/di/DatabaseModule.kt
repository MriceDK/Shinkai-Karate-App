package be.mauricedeke.shinkai.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import be.mauricedeke.shinkai.data.local.LocationSettingsDao
import be.mauricedeke.shinkai.data.local.NoteDao
import be.mauricedeke.shinkai.data.local.NotificationSettingsDao
import be.mauricedeke.shinkai.data.local.ShinkaiDatabase
import be.mauricedeke.shinkai.data.local.ThemePreferenceDao
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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShinkaiDatabase =
        Room.databaseBuilder(context, ShinkaiDatabase::class.java, "shinkai_db")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
            .build()

    @Provides
    fun provideNoteDao(db: ShinkaiDatabase): NoteDao = db.noteDao()

    @Provides
    fun provideThemePreferenceDao(db: ShinkaiDatabase): ThemePreferenceDao = db.themePreferenceDao()

    @Provides
    fun provideNotificationSettingsDao(db: ShinkaiDatabase): NotificationSettingsDao = db.notificationSettingsDao()

    @Provides
    fun provideLocationSettingsDao(db: ShinkaiDatabase): LocationSettingsDao = db.locationSettingsDao()
}
