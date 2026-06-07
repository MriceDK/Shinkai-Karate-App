package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.data.remote.client.TrainingSessionClient
import be.mauricedeke.shinkai.domain.repository.EventRepository
import be.mauricedeke.shinkai.domain.repository.GeocodingRepository
import be.mauricedeke.shinkai.geofence.GeofenceItem
import be.mauricedeke.shinkai.geofence.GeofenceManager
import javax.inject.Inject

class SetupGeofencesUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val trainingSessionClient: TrainingSessionClient,
    private val geocodingRepository: GeocodingRepository,
    private val geofenceManager: GeofenceManager
) {
    suspend operator fun invoke() {
        val items = mutableListOf<GeofenceItem>()

        eventRepository.getEvents()?.forEach { event ->
            if (event.lat != null && event.lng != null) {
                items.add(
                    GeofenceItem(
                        id = event.id.toString(),
                        name = event.title,
                        logType = event.title,
                        lat = event.lat,
                        lng = event.lng,
                        date = event.date,
                        startTime = event.startTime,
                        endTime = event.endTime
                    )
                )
            }
        }

        trainingSessionClient.getTrainingSessions().getOrNull()?.forEach { dto ->
            if (dto.location.isNotBlank()) {
                val coords = geocodingRepository.geocode(dto.location)
                if (coords != null) {
                    items.add(
                        GeofenceItem(
                            id = dto.id,
                            name = "${dto.type} training",
                            logType = dto.type,
                            lat = coords.first,
                            lng = coords.second,
                            date = dto.date,
                            startTime = dto.startTime,
                            endTime = dto.endTime
                        )
                    )
                }
            }
        }

        geofenceManager.setup(items)
    }
}
