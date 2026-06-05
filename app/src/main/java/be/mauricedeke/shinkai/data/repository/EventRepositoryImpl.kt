package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.repository.EventRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor() : EventRepository {

    private val allEvents get() = (FakeDataSource.events ?: emptyList()) + (FakeDataSource.inboxEvents ?: emptyList())

    override suspend fun getEvents(): List<Event>? = FakeDataSource.events
    override suspend fun getInboxEvents(): List<Event>? = FakeDataSource.inboxEvents
    override suspend fun getEventById(id: UUID): Event? = allEvents.find { it.id == id }

    override suspend fun setRsvp(id: UUID, attending: Boolean?) {
        fun MutableList<Event>.updateRsvp() {
            val i = indexOfFirst { it.id == id }
            if (i >= 0) this[i] = this[i].copy(rsvp = attending)
        }
        FakeDataSource.events.updateRsvp()
        FakeDataSource.inboxEvents.updateRsvp()
    }
}
