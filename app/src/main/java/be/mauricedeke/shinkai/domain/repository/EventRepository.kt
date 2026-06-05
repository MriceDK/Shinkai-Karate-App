package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.Event
import java.util.UUID

interface EventRepository {
    suspend fun getEvents(): List<Event>?
    suspend fun getEventById(id: UUID): Event?
    suspend fun getInboxEvents(): List<Event>?
    suspend fun setRsvp(id: UUID, attending: Boolean?)
}
