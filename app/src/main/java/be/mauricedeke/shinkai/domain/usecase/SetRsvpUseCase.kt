package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.EventRepository
import javax.inject.Inject

class SetRsvpUseCase @Inject constructor(private val eventRepository: EventRepository) {
    suspend operator fun invoke(id: String, attending: Boolean?) = eventRepository.setRsvp(id, attending)
}
