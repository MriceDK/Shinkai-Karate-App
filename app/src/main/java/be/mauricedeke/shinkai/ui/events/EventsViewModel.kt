package be.mauricedeke.shinkai.ui.events

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
class EventsViewModel @Inject constructor(
    private val getEvents: GetEventsUseCase,
    private val getInboxEvents: GetInboxEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState

    init {
        viewModelScope.launch {
            val today = LocalDate.now()
            fun isUpcoming(date: LocalDate?) = date == null || !date.isBefore(today)
            val events = getEvents()
            val inboxEvents = getInboxEvents()
            _uiState.update {
                it.copy(
                    upcomingEvents = events?.filter { e -> isUpcoming(e.localDate) }?.take(4) ?: emptyList(),
                    allUpcomingEvents = events?.filter { e -> isUpcoming(e.localDate) } ?: emptyList(),
                    inboxEvents = inboxEvents?.filter { e -> isUpcoming(e.localDate) } ?: emptyList(),
                    selectedDate = today,
                    isError = events == null || inboxEvents == null
                )
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }
}
