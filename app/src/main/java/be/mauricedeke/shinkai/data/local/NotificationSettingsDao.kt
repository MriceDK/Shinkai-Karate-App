package be.mauricedeke.shinkai.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NotificationSettingsDao {
    @Query("SELECT * FROM notification_settings WHERE id = 0")
    suspend fun get(): NotificationSettingsEntity?

    @Upsert
    suspend fun upsert(entity: NotificationSettingsEntity)
}
