package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.remote.client.EventClient
import be.mauricedeke.shinkai.data.remote.mapper.toDomain
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.repository.EventRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventClient: EventClient
) : EventRepository {

    override suspend fun getEvents(): List<Event>? =
        eventClient.getEvents().getOrNull()?.map { it.toDomain() }

    override suspend fun getEventById(id: UUID): Event? =
        eventClient.getEventById(id.toString()).getOrNull()?.toDomain()

    override suspend fun setRsvp(id: UUID, attending: Boolean?) {
        if (attending != null) {
            eventClient.setRsvp(id.toString(), attending)
        }
    }
}
