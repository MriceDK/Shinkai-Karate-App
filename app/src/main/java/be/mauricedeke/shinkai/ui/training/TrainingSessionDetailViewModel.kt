package be.mauricedeke.shinkai.ui.training

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetTrainingSessionByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingSessionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTrainingSessionById: GetTrainingSessionByIdUseCase
) : ViewModel() {

    private val sessionId: String = checkNotNull(savedStateHandle["sessionId"])

    private val _uiState = MutableStateFlow(TrainingSessionDetailUiState())
    val uiState: StateFlow<TrainingSessionDetailUiState> = _uiState

    init {
        viewModelScope.launch {
            val session = getTrainingSessionById(sessionId)
            if (session != null) {
                _uiState.update { it.copy(session = session, isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }
}
