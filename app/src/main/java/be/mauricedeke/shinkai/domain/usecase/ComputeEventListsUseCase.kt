package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Event
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class EventListsResult(
    val upcomingEvents: List<Event>,
    val allUpcomingEvents: List<Event>,
    val inboxEvents: List<Event>
)

class ComputeEventListsUseCase @Inject constructor() {
    operator fun invoke(
        events: List<Event>,
        rsvp: Map<UUID, Boolean?>
    ): EventListsResult {
        val today = LocalDate.now()
        val upcoming = events
            .filter { it.localDate == null || !it.localDate.isBefore(today) }
            .sortedWith(compareBy(nullsLast()) { it.localDate })

        val pending = upcoming.filter { rsvp[it.id] == null }

        return EventListsResult(
            upcomingEvents = upcoming.take(4),
            allUpcomingEvents = upcoming,
            inboxEvents = pending
        )
    }
}
