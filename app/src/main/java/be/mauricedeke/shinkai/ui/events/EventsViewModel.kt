package be.mauricedeke.shinkai.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.usecase.ComputeEventListsUseCase
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
class EventsViewModel @Inject constructor(
    private val getEvents: GetEventsUseCase,
    private val getInboxEvents: GetInboxEventsUseCase,
    private val setRsvpUseCase: SetRsvpUseCase,
    private val computeEventLists: ComputeEventListsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState

    private var cachedRegularEvents: List<Event> = emptyList()
    private var cachedInboxEvents: List<Event> = emptyList()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isError = false) }
            val today = LocalDate.now()
            val events = getEvents()
            val inbox = getInboxEvents()

            if (events == null || inbox == null) {
                _uiState.update { it.copy(isError = true, selectedDate = it.selectedDate ?: today) }
                return@launch
            }

            cachedRegularEvents = events
            cachedInboxEvents = inbox

            val rsvp = (events + inbox).associate { it.id to it.rsvp }
            _uiState.update { it.copy(rsvp = rsvp, selectedDate = today, isError = false) }
            applyEventLists(rsvp)
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun setRsvp(eventId: UUID, attending: Boolean) {
        val current = _uiState.value.rsvp[eventId]
        val next = if (current == attending) null else attending
        val newRsvp = _uiState.value.rsvp + (eventId to next)
        _uiState.update { it.copy(rsvp = newRsvp) }
        applyEventLists(newRsvp)
        viewModelScope.launch { setRsvpUseCase(eventId, next) }
    }

    private fun applyEventLists(rsvp: Map<UUID, Boolean?>) {
        val result = computeEventLists(cachedRegularEvents, cachedInboxEvents, rsvp)
        _uiState.update { state ->
            state.copy(
                upcomingEvents = result.upcomingEvents,
                allUpcomingEvents = result.allUpcomingEvents,
                inboxEvents = result.inboxEvents
            )
        }
    }
}
