package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import be.mauricedeke.shinkai.data.local.room.entity.LocationSettingsEntity

@Dao
interface LocationSettingsDao {
    @Query("SELECT * FROM location_settings WHERE id = 0")
    suspend fun get(): LocationSettingsEntity?

    @Upsert
    suspend fun upsert(entity: LocationSettingsEntity)
}
