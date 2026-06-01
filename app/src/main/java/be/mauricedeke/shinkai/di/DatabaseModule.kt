package be.mauricedeke.shinkai.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import be.mauricedeke.shinkai.data.local.NoteDao
import be.mauricedeke.shinkai.data.local.ThemePreferenceDao
import be.mauricedeke.shinkai.data.local.ShinkaiDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShinkaiDatabase =
        Room.databaseBuilder(context, ShinkaiDatabase::class.java, "shinkai_db")
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    fun provideNoteDao(db: ShinkaiDatabase): NoteDao = db.noteDao()

    @Provides
    fun provideThemePreferenceDao(db: ShinkaiDatabase): ThemePreferenceDao = db.themePreferenceDao()
}
