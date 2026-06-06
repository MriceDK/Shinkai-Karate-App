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

    private var eventsCache: MutableList<Event>? = null
    private var inboxCache: MutableList<Event>? = null

    override suspend fun getEvents(): List<Event>? {
        if (eventsCache == null) {
            eventsCache = eventClient.getEvents().getOrNull()
                ?.map { it.toDomain() }?.toMutableList()
        }
        return eventsCache
    }

    override suspend fun getInboxEvents(): List<Event>? {
        if (inboxCache == null) {
            inboxCache = eventClient.getInboxEvents().getOrNull()
                ?.map { it.toDomain() }?.toMutableList()
        }
        return inboxCache
    }

    override suspend fun getEventById(id: UUID): Event? {
        val fromCache = (eventsCache.orEmpty() + inboxCache.orEmpty()).find { it.id == id }
        if (fromCache != null) return fromCache
        return eventClient.getEventById(id.toString()).getOrNull()?.toDomain()
    }

    override suspend fun setRsvp(id: UUID, attending: Boolean?) {
        if (attending != null) {
            eventClient.setRsvp(id.toString(), attending)
        }
        eventsCache?.replaceRsvp(id, attending)
        inboxCache?.replaceRsvp(id, attending)
    }

    private fun MutableList<Event>.replaceRsvp(id: UUID, attending: Boolean?) {
        val i = indexOfFirst { it.id == id }
        if (i >= 0) this[i] = this[i].copy(rsvp = attending)
    }
}
