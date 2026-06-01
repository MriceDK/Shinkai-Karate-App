package be.mauricedeke.shinkai.domain.model

data class LocationSettings(
    val useForTrainingLocations: Boolean = true,
    val useForImprovements: Boolean = true,
    val useForTrackingTrainings: Boolean = true
)
