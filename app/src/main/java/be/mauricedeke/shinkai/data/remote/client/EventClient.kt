package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.EventApi
import be.mauricedeke.shinkai.data.remote.dto.EventDto
import be.mauricedeke.shinkai.data.remote.dto.RsvpRequestDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventClient @Inject constructor(private val api: EventApi) {

    suspend fun getEvents(): Result<List<EventDto>> =
        runCatching { api.getEvents() }

    suspend fun getInboxEvents(): Result<List<EventDto>> =
        runCatching { api.getInboxEvents() }

    suspend fun getEventById(id: String): Result<EventDto> =
        runCatching { api.getEventById(id) }

    suspend fun setRsvp(id: String, attending: Boolean): Result<Unit> =
        runCatching { api.setRsvp(id, RsvpRequestDto(attending)) }
}
