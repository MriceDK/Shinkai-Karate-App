package be.mauricedeke.shinkai.domain.model

data class LocationSettings(
    val showOnMap: Boolean = true,
    val useForGeofencing: Boolean = true,
    val useForStatistics: Boolean = true
)
