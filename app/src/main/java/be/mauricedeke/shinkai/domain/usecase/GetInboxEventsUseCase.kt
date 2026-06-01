package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.repository.EventRepository
import javax.inject.Inject

class GetInboxEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(): List<Event> = eventRepository.getInboxEvents()
}
