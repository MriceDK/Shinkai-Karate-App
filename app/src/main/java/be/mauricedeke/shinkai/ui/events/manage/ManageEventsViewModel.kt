package be.mauricedeke.shinkai.ui.events.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetEventsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetInboxEventsUseCase
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
    private val getEvents: GetEventsUseCase,
    private val getInboxEvents: GetInboxEventsUseCase,
    private val setRsvpUseCase: SetRsvpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageEventsUiState())
    val uiState: StateFlow<ManageEventsUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val events = getEvents()
            val inboxEvents = getInboxEvents()
            if (events == null || inboxEvents == null) {
                _uiState.update { it.copy(isError = true) }
                return@launch
            }
            val today = LocalDate.now()
            val cutoff = today.minusMonths(1)
            val filtered = (events + inboxEvents)
                .filter { it.localDate == null || !it.localDate.isBefore(cutoff) }
            val (upcoming, past) = filtered.partition { it.localDate == null || !it.localDate.isBefore(today) }
            val sorted = upcoming.sortedWith(compareBy(nullsLast()) { it.localDate }) +
                         past.sortedByDescending { it.localDate }
            _uiState.update { it.copy(events = sorted, rsvp = sorted.associate { e -> e.id to e.rsvp }) }
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

