package be.mauricedeke.shinkai.ui.events.detail

import be.mauricedeke.shinkai.domain.model.Event

data class EventDetailUiState(
    val event: Event = Event(),
    val rsvp: Boolean? = null
)
