package be.mauricedeke.shinkai.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NoteDao {
    @Query("SELECT note FROM belt_notes WHERE beltName = :beltName")
    suspend fun getNote(beltName: String): String?

    @Upsert
    suspend fun upsertNote(entity: BeltNoteEntity)
}
