package be.mauricedeke.shinkai.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StrengthResultDao {
    @Query("SELECT * FROM strength_results ORDER BY type")
    fun observeAll(): Flow<List<StrengthResultEntity>>

    @Upsert
    suspend fun upsert(entity: StrengthResultEntity)
}
