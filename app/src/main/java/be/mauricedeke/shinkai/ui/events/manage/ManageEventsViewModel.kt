package be.mauricedeke.shinkai.ui.events.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetManagedEventsUseCase
import be.mauricedeke.shinkai.domain.usecase.SetRsvpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ManageEventsViewModel @Inject constructor(
    private val getManagedEvents: GetManagedEventsUseCase,
    private val setRsvpUseCase: SetRsvpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageEventsUiState())
    val uiState: StateFlow<ManageEventsUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isError = false, isRefreshing = true) }
            val result = getManagedEvents()
            if (result == null) {
                _uiState.update { it.copy(isError = true, isRefreshing = false) }
                return@launch
            }
            _uiState.update { it.copy(events = result.events, rsvp = result.rsvp, isError = false, isRefreshing = false) }
        }
    }

    fun setRsvp(eventId: UUID, attending: Boolean) {
        val event = _uiState.value.events.find { it.id == eventId } ?: return
        val isUpcoming = event.localDate == null || !event.localDate.isBefore(LocalDate.now())
        if (!isUpcoming) return

        val current = _uiState.value.rsvp[eventId]
        val next = if (current == attending) null else attending
        _uiState.update { state -> state.copy(rsvp = state.rsvp + (eventId to next)) }
        viewModelScope.launch { setRsvpUseCase(eventId, next) }
    }
}
