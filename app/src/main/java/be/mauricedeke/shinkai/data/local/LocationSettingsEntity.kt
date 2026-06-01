package be.mauricedeke.shinkai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_settings")
data class LocationSettingsEntity(
    @PrimaryKey val id: Int = 0,
    val useForTrainingLocations: Boolean = true,
    val useForImprovements: Boolean = true,
    val useForTrackingTrainings: Boolean = true
)
