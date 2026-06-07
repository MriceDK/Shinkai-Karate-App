package be.mauricedeke.shinkai.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetEventsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetNextTrainingUseCase
import be.mauricedeke.shinkai.domain.usecase.GetShortcutsUseCase
import be.mauricedeke.shinkai.domain.usecase.UpdateShortcutsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getEvents: GetEventsUseCase,
    private val getNextTraining: GetNextTrainingUseCase,
    private val getShortcuts: GetShortcutsUseCase,
    private val updateShortcuts: UpdateShortcutsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            val storedIds = getShortcuts()
            val shortcuts = storedIds.mapNotNull { runCatching { ShortcutId.valueOf(it) }.getOrNull() }
                .ifEmpty { defaultShortcuts }
            _uiState.update { it.copy(shortcuts = shortcuts) }
        }
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isEventsError = false) }
            val today = LocalDate.now()
            val events = getEvents()
            val upcomingEvents = events
                ?.filter { it.localDate == null || !it.localDate.isBefore(today) }
                ?.take(2)
                ?: emptyList()
            val nextTraining = getNextTraining()
            _uiState.update {
                it.copy(
                    upcomingEvents = upcomingEvents,
                    nextTraining = nextTraining,
                    isEventsError = events == null
                )
            }
        }
    }

    fun toggleShortcut(id: ShortcutId) {
        val current = _uiState.value.shortcuts.toMutableList()
        if (current.contains(id)) {
            current.remove(id)
        } else if (current.size < MAX_SHORTCUTS) {
            current.add(id)
        }
        _uiState.update { it.copy(shortcuts = current) }
        viewModelScope.launch {
            updateShortcuts(current.map { it.name })
        }
    }
}
