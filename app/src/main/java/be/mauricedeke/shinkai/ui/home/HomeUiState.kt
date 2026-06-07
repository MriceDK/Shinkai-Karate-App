package be.mauricedeke.shinkai.ui.home

import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.model.TrainingSession

data class HomeUiState(
    val upcomingEvents: List<Event> = emptyList(),
    val nextTraining: TrainingSession? = null,
    val isEventsError: Boolean = false,
    val isTrainingError: Boolean = false,
    val isRefreshing: Boolean = false,
    val shortcuts: List<ShortcutId> = defaultShortcuts
)
