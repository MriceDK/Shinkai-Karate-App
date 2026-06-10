package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import be.mauricedeke.shinkai.data.local.room.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 0 LIMIT 1")
    suspend fun get(): UserProfileEntity?

    @Query("SELECT * FROM user_profile WHERE id = 0 LIMIT 1")
    fun observe(): Flow<UserProfileEntity?>

    @Upsert
    suspend fun upsert(entity: UserProfileEntity)

    @Query("DELETE FROM user_profile")
    suspend fun clear()
}
