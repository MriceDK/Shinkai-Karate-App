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

    @Query("SELECT * FROM belts WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): BeltEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(belts: List<BeltEntity>)

    @Query("DELETE FROM belts")
    suspend fun deleteAll()
}
