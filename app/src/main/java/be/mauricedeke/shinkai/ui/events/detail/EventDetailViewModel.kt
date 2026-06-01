package be.mauricedeke.shinkai.ui.events.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetEventByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val getEventById: GetEventByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState

    fun loadEvent(id: String) {
        viewModelScope.launch {
            val event = getEventById(id)
            if (event != null) _uiState.update { it.copy(event = event) }
        }
    }
}
