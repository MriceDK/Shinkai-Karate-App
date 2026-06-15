package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.EventRepository
import be.mauricedeke.shinkai.data.geofence.GeofenceItem
import be.mauricedeke.shinkai.data.geofence.GeofenceManager
import java.time.LocalDate
import javax.inject.Inject

class SetupGeofencesUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val getNotificationSettings: GetNotificationSettingsUseCase,
    private val getLocationSettings: GetLocationSettingsUseCase,
    private val geofenceManager: GeofenceManager
) {
    suspend operator fun invoke() {
        val notifSettings = getNotificationSettings()
        val locationSettings = getLocationSettings()
        if (!notifSettings.eventNotifications || !locationSettings.useForGeofencing) {
            geofenceManager.setup(emptyList())
            return
        }

        val today = LocalDate.now()
        val items = eventRepository.getEvents()
            ?.filter { event ->
                event.lat != null && event.lng != null &&
                        (event.localDate == null || !event.localDate.isBefore(today))
            }
            ?.map { event ->
                GeofenceItem(
                    id = event.id.toString(),
                    name = event.title,
                    logType = event.title,
                    lat = event.lat!!,
                    lng = event.lng!!,
                    date = event.date,
                    startTime = event.startTime,
                    endTime = event.endTime
                )
            }
            ?: emptyList()

        geofenceManager.setup(items)
    }
}
