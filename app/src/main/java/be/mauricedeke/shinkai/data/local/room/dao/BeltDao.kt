package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.mauricedeke.shinkai.data.local.room.entity.BeltEntity

@Dao
interface BeltDao {
    @Query("SELECT * FROM belts")
    suspend fun getAll(): List<BeltEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(belts: List<BeltEntity>)

    @Query("DELETE FROM belts")
    suspend fun deleteAll()
}
