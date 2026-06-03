package be.mauricedeke.shinkai.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.usecase.GetEventsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetInboxEventsUseCase
import be.mauricedeke.shinkai.domain.usecase.SetRsvpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val getEvents: GetEventsUseCase,
    private val getInboxEvents: GetInboxEventsUseCase,
    private val setRsvpUseCase: SetRsvpUseCase
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
            _uiState.update { it.copy(rsvp = rsvp, selectedDate = today) }
            recomputeLists(rsvp)
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun setRsvp(eventId: String, attending: Boolean) {
        val current = _uiState.value.rsvp[eventId]
        val next = if (current == attending) null else attending
        val newRsvp = _uiState.value.rsvp + (eventId to next)
        _uiState.update { it.copy(rsvp = newRsvp) }
        recomputeLists(newRsvp)
        viewModelScope.launch { setRsvpUseCase(eventId, next) }
    }

    private fun recomputeLists(rsvp: Map<String, Boolean?>) {
        val today = LocalDate.now()
        fun isUpcoming(date: LocalDate?) = date == null || !date.isBefore(today)

        val upcomingInbox = cachedInboxEvents.filter { isUpcoming(it.localDate) }
        val upcomingRegular = cachedRegularEvents.filter { isUpcoming(it.localDate) }
        val allUpcoming = (upcomingRegular + upcomingInbox)
            .sortedWith(compareBy(nullsLast()) { it.localDate })
        val pendingInbox = allUpcoming.filter { rsvp[it.id] == null }

        _uiState.update { state ->
            state.copy(
                upcomingEvents = allUpcoming.take(4),
                allUpcomingEvents = allUpcoming,
                inboxEvents = pendingInbox
            )
        }
    }
}
