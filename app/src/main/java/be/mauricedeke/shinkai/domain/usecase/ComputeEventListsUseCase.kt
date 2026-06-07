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
        regularEvents: List<Event>,
        inboxEvents: List<Event>,
        rsvp: Map<UUID, Boolean?>
    ): EventListsResult {
        val today = LocalDate.now()
        fun isUpcoming(date: LocalDate?) = date == null || !date.isBefore(today)

        val allUpcoming = (regularEvents.filter { isUpcoming(it.localDate) } +
                inboxEvents.filter { isUpcoming(it.localDate) })
            .sortedWith(compareBy(nullsLast()) { it.localDate })
        val pendingInbox = allUpcoming.filter { rsvp[it.id] == null }

        return EventListsResult(
            upcomingEvents = allUpcoming.take(4),
            allUpcomingEvents = allUpcoming,
            inboxEvents = pendingInbox
        )
    }
}
