package be.mauricedeke.shinkai.ui.profiel.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.model.Training
import be.mauricedeke.shinkai.domain.usecase.AddTrainingUseCase
import be.mauricedeke.shinkai.domain.usecase.GetAllTrainingsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetTrainingNoteUseCase
import be.mauricedeke.shinkai.domain.usecase.GetTrainingsByDateUseCase
import be.mauricedeke.shinkai.domain.usecase.SaveTrainingNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TrainingHistoryViewModel @Inject constructor(
    private val getAllTrainings: GetAllTrainingsUseCase,
    private val getTrainingsByDate: GetTrainingsByDateUseCase,
    private val addTraining: AddTrainingUseCase,
    private val getTrainingNote: GetTrainingNoteUseCase,
    private val saveTrainingNote: SaveTrainingNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingHistoryUiState())
    val uiState: StateFlow<TrainingHistoryUiState> = _uiState

    init {
        viewModelScope.launch {
            val today = LocalDate.now()
            val all = getAllTrainings()
            val dates = all?.map { it.date }?.toSet() ?: emptySet()
            val todayTrainings = all?.filter { it.date == today } ?: emptyList()
            _uiState.update {
                it.copy(
                    selectedDate = today,
                    selectedTrainings = todayTrainings,
                    trainingDates = dates,
                    isError = all == null
                )
            }
        }
    }

    fun resetToToday() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val trainings = getTrainingsByDate(today)
            _uiState.update {
                it.copy(
                    selectedDate = today,
                    selectedTrainings = trainings ?: emptyList(),
                    selectedTrainingId = null,
                    notes = "",
                    isError = trainings == null
                )
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        viewModelScope.launch {
            val trainings = getTrainingsByDate(date)
            _uiState.update {
                it.copy(
                    selectedDate = date,
                    selectedTrainings = trainings ?: emptyList(),
                    selectedTrainingId = null,
                    notes = "",
                    isError = trainings == null
                )
            }
        }
    }

    fun onTrainingSelected(id: UUID) {
        val current = _uiState.value.selectedTrainingId
        if (current == id) {
            _uiState.update { it.copy(selectedTrainingId = null, notes = "") }
        } else {
            _uiState.update { it.copy(selectedTrainingId = id, notes = getTrainingNote(id)) }
        }
    }

    fun onNotesChanged(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun onNotesFocusLost() {
        val id = _uiState.value.selectedTrainingId ?: return
        saveTrainingNote(id, _uiState.value.notes)
    }

    fun showLogSheet() = _uiState.update { it.copy(showLogSheet = true) }
    fun dismissLogSheet() = _uiState.update { it.copy(showLogSheet = false) }

    fun logTraining(type: String, startTime: String, endTime: String, sensei: String, injuries: String) {
        val date = _uiState.value.selectedDate
        val training = Training(
            id = UUID.randomUUID(),
            type = type,
            startTime = startTime,
            endTime = endTime,
            date = date,
            sensei = sensei.ifBlank { "Geen" },
            injuries = injuries.ifBlank { "Geen" }
        )
        viewModelScope.launch {
            addTraining(training)
            val updated = getTrainingsByDate(date) ?: emptyList()
            _uiState.update {
                it.copy(
                    selectedTrainings = updated,
                    trainingDates = it.trainingDates + date,
                    showLogSheet = false
                )
            }
        }
    }
}
