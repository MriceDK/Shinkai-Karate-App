package be.mauricedeke.shinkai.ui.profiel.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetTrainingsByDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrainingHistoryViewModel @Inject constructor(
    private val getTrainingsByDate: GetTrainingsByDateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingHistoryUiState())
    val uiState: StateFlow<TrainingHistoryUiState> = _uiState

    fun onDateSelected(date: LocalDate) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedDate = date, selectedTrainings = getTrainingsByDate(date)) }
        }
    }
}
