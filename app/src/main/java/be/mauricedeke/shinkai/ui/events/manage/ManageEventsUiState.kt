package be.mauricedeke.shinkai.ui.events.manage

import be.mauricedeke.shinkai.domain.model.Event

data class ManageEventsUiState(
    val events: List<Event> = emptyList(),
    val rsvp: Map<String, Boolean?> = emptyMap(), // true = attending, false = not attending, null = no response
    val isError: Boolean = false
)
