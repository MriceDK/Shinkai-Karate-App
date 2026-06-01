package be.mauricedeke.shinkai.ui.home

import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.model.Training

data class HomeUiState(
    val upcomingEvents: List<Event> = emptyList(),
    val nextTraining: Training? = null
)
