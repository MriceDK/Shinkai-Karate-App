package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.mauricedeke.shinkai.data.local.room.entity.LexiconEntryEntity

@Dao
interface LexiconDao {
    @Query("SELECT * FROM lexicon_entries")
    suspend fun getAll(): List<LexiconEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entries: List<LexiconEntryEntity>)

    @Query("DELETE FROM lexicon_entries")
    suspend fun deleteAll()
}
