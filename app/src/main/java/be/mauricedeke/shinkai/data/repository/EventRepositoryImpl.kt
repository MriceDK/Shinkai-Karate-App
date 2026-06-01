package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.repository.EventRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor() : EventRepository {
    override suspend fun getEvents(): List<Event>? = FakeDataSource.events
    override suspend fun getEventById(id: String): Event? =
        ((FakeDataSource.events ?: emptyList()) + (FakeDataSource.inboxEvents ?: emptyList())).find { it.id == id }
    override suspend fun getInboxEvents(): List<Event>? = FakeDataSource.inboxEvents
}
