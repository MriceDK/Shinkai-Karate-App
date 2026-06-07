package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.mauricedeke.shinkai.data.local.room.entity.KataEntity

@Dao
interface KataDao {
    @Query("SELECT * FROM katas")
    suspend fun getAll(): List<KataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(katas: List<KataEntity>)

    @Query("DELETE FROM katas")
    suspend fun deleteAll()
}
