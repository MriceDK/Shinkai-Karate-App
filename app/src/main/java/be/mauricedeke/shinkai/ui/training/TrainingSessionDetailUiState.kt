package be.mauricedeke.shinkai.ui.training

import be.mauricedeke.shinkai.domain.model.TrainingSession

data class TrainingSessionDetailUiState(
    val session: TrainingSession = TrainingSession(),
    val isLoading: Boolean = true,
    val isError: Boolean = false
)
