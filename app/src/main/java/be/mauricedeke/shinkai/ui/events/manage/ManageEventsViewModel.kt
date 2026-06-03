package be.mauricedeke.shinkai.ui.events.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetEventsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetInboxEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ManageEventsViewModel @Inject constructor(
    private val getEvents: GetEventsUseCase,
    private val getInboxEvents: GetInboxEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageEventsUiState())
    val uiState: StateFlow<ManageEventsUiState> = _uiState

    init {
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
            val initialRsvp = sorted.associate { it.id to it.rsvp }
            _uiState.update { it.copy(events = sorted, rsvp = initialRsvp) }
        }
    }

    fun setRsvp(eventId: String, attending: Boolean) {
        val event = _uiState.value.events.find { it.id == eventId } ?: return
        val isUpcoming = event.localDate == null || !event.localDate.isBefore(LocalDate.now())
        if (!isUpcoming) return

        _uiState.update { state ->
            val current = state.rsvp[eventId]
            val next = if (current == attending) null else attending
            state.copy(rsvp = state.rsvp + (eventId to next))
        }
    }
}
