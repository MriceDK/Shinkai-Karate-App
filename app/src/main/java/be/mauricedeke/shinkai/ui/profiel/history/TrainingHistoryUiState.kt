package be.mauricedeke.shinkai.ui.profiel.history

import be.mauricedeke.shinkai.domain.model.Training
import java.time.LocalDate

data class TrainingHistoryUiState(
    val selectedDate: LocalDate? = LocalDate.of(2025, 8, 14),
    val selectedTrainings: List<Training> = emptyList(),
    val isError: Boolean = false
)
