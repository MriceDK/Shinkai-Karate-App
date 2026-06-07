package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import be.mauricedeke.shinkai.data.local.room.entity.StrengthResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StrengthResultDao {
    @Query("SELECT * FROM strength_results ORDER BY type")
    fun observeAll(): Flow<List<StrengthResultEntity>>

    @Query("SELECT bestScore FROM strength_results WHERE type = :type LIMIT 1")
    suspend fun getBestScore(type: String): Int?

    @Upsert
    suspend fun upsert(entity: StrengthResultEntity)
}
