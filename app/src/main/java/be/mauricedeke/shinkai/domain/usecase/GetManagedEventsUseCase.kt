package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.repository.EventRepository
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class ManagedEventsResult(
    val events: List<Event>,
    val rsvp: Map<UUID, Boolean?>
)

class GetManagedEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(): ManagedEventsResult? {
        val events = eventRepository.getEvents() ?: return null
        val inboxEvents = eventRepository.getInboxEvents() ?: return null

        val today = LocalDate.now()
        val cutoff = today.minusMonths(1)
        val filtered = (events + inboxEvents)
            .filter { it.localDate == null || !it.localDate.isBefore(cutoff) }
        val (upcoming, past) = filtered.partition { it.localDate == null || !it.localDate.isBefore(today) }
        val sorted = upcoming.sortedWith(compareBy(nullsLast()) { it.localDate }) +
                     past.sortedByDescending { it.localDate }

        return ManagedEventsResult(
            events = sorted,
            rsvp = sorted.associate { it.id to it.rsvp }
        )
    }
}
