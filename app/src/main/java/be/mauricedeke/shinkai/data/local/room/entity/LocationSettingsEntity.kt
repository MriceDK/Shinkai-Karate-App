package be.mauricedeke.shinkai.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_settings")
data class LocationSettingsEntity(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "useForTrainingLocations") val showOnMap: Boolean = true,
    @ColumnInfo(name = "useForImprovements") val useForStatistics: Boolean = true,
    @ColumnInfo(name = "useForTrackingTrainings") val useForGeofencing: Boolean = true
)
