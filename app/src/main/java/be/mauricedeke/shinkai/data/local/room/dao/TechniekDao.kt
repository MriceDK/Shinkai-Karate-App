package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.mauricedeke.shinkai.data.local.room.entity.TechniekEntity

@Dao
interface TechniekDao {
    @Query("SELECT * FROM technieken")
    suspend fun getAll(): List<TechniekEntity>

    @Query("SELECT * FROM technieken WHERE beltName = :beltName")
    suspend fun getByBelt(beltName: String): List<TechniekEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(technieken: List<TechniekEntity>)

    @Query("DELETE FROM technieken")
    suspend fun deleteAll()
}
