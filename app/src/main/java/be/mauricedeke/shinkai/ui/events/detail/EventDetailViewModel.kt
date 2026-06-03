package be.mauricedeke.shinkai.ui.events.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetEventByIdUseCase
import be.mauricedeke.shinkai.domain.usecase.SetRsvpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val getEventById: GetEventByIdUseCase,
    private val setRsvpUseCase: SetRsvpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState

    fun loadEvent(id: String) {
        viewModelScope.launch {
            val event = getEventById(id)
            if (event != null) _uiState.update { it.copy(event = event, rsvp = event.rsvp) }
        }
    }

    fun setRsvp(attending: Boolean) {
        val event = _uiState.value.event
        val isUpcoming = event.localDate == null || !event.localDate.isBefore(LocalDate.now())
        if (!isUpcoming) return

        val current = _uiState.value.rsvp
        val next = if (current == attending) null else attending
        _uiState.update { it.copy(rsvp = next) }
        viewModelScope.launch { setRsvpUseCase(event.id, next) }
    }
}
