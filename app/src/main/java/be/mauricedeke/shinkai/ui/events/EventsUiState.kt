package be.mauricedeke.shinkai.ui.events

import be.mauricedeke.shinkai.domain.model.Event
import java.time.LocalDate

data class EventsUiState(
    val upcomingEvents: List<Event> = emptyList(),
    val allUpcomingEvents: List<Event> = emptyList(),
    val inboxEvents: List<Event> = emptyList(),
    val rsvp: Map<String, Boolean?> = emptyMap(),
    val selectedDate: LocalDate? = null,
    val isError: Boolean = false
)
