package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Event
import javax.inject.Inject

class GetInboxEventsUseCase @Inject constructor() {
    suspend operator fun invoke(): List<Event> = emptyList()
}
