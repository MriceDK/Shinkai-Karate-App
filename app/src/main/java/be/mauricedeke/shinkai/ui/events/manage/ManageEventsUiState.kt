package be.mauricedeke.shinkai.ui.events.manage

import be.mauricedeke.shinkai.domain.model.Event
import java.util.UUID

data class ManageEventsUiState(
    val events: List<Event> = emptyList(),
    val rsvp: Map<UUID, Boolean?> = emptyMap(),
    val isError: Boolean = false,
    val isRefreshing: Boolean = false
)
