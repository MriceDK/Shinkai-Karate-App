package be.mauricedeke.shinkai.ui.technieken.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetNoteUseCase
import be.mauricedeke.shinkai.domain.usecase.GetTechnieksByBeltUseCase
import be.mauricedeke.shinkai.domain.usecase.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechniekDetailViewModel @Inject constructor(
    private val getTechnieksByBelt: GetTechnieksByBeltUseCase,
    private val getNote: GetNoteUseCase,
    private val saveNote: SaveNoteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TechniekDetailUiState())
    val uiState: StateFlow<TechniekDetailUiState> = _uiState

    fun loadBelt(belt: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    belt = belt,
                    technieken = getTechnieksByBelt(belt),
                    notes = getNote(belt)
                )
            }
        }
    }

    fun onNotesChanged(notes: String) {
        _uiState.update { it.copy(notes = notes) }
        viewModelScope.launch {
            saveNote(_uiState.value.belt, notes)
        }
    }
}
