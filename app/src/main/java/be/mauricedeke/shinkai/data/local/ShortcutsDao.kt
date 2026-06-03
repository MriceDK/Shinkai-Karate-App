package be.mauricedeke.shinkai.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ShortcutsDao {
    @Query("SELECT * FROM shortcuts WHERE id = 0")
    suspend fun get(): ShortcutsEntity?

    @Upsert
    suspend fun upsert(entity: ShortcutsEntity)
}
