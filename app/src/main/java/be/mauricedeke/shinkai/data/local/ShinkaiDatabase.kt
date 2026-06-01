package be.mauricedeke.shinkai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BeltNoteEntity::class, ThemePreferenceEntity::class], version = 2, exportSchema = false)
abstract class ShinkaiDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun themePreferenceDao(): ThemePreferenceDao
}
