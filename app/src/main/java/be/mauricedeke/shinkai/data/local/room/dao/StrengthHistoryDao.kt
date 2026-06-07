package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import be.mauricedeke.shinkai.data.local.room.entity.StrengthHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StrengthHistoryDao {
    @Query("SELECT * FROM strength_history ORDER BY timestamp DESC LIMIT 30")
    fun observeRecent(): Flow<List<StrengthHistoryEntity>>

    @Insert
    suspend fun insert(entity: StrengthHistoryEntity)
}
