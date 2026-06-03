package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.Event

interface EventRepository {
    suspend fun getEvents(): List<Event>?
    suspend fun getEventById(id: String): Event?
    suspend fun getInboxEvents(): List<Event>?
    suspend fun setRsvp(id: String, attending: Boolean?)
}
