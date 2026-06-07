package be.mauricedeke.shinkai.geofence

data class GeofenceItem(
    val id: String,
    val name: String,
    val logType: String,
    val lat: Double,
    val lng: Double,
    val date: String,
    val startTime: String,
    val endTime: String
)
