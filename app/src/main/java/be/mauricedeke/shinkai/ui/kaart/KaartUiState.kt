package be.mauricedeke.shinkai.ui.kaart

import be.mauricedeke.shinkai.domain.model.Event

data class KaartUiState(
    val showEventDetail: Boolean = false,
    val events: List<Event> = emptyList()
)
