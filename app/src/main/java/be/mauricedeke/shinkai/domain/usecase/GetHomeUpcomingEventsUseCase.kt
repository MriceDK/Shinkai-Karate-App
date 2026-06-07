package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.repository.EventRepository
import java.time.LocalDate
import javax.inject.Inject

class GetHomeUpcomingEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(): List<Event>? {
        val today = LocalDate.now()
        return eventRepository.getEvents()
            ?.filter { it.localDate == null || !it.localDate.isBefore(today) }
            ?.take(2)
    }
}
