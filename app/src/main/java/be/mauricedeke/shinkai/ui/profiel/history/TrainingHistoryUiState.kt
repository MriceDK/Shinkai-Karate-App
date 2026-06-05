package be.mauricedeke.shinkai.ui.profiel.history

import be.mauricedeke.shinkai.domain.model.Training
import java.time.LocalDate
import java.util.UUID

data class TrainingHistoryUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedTrainings: List<Training> = emptyList(),
    val trainingDates: Set<LocalDate> = emptySet(),
    val selectedTrainingId: UUID? = null,
    val notes: String = "",
    val showLogSheet: Boolean = false,
    val isError: Boolean = false
)
