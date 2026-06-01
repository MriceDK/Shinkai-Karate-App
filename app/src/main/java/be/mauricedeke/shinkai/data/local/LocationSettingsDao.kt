package be.mauricedeke.shinkai.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface LocationSettingsDao {
    @Query("SELECT * FROM location_settings WHERE id = 0")
    suspend fun get(): LocationSettingsEntity?

    @Upsert
    suspend fun upsert(entity: LocationSettingsEntity)
}
