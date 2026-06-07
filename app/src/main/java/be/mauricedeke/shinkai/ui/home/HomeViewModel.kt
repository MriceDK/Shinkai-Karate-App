package be.mauricedeke.shinkai.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetHomeUpcomingEventsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetNextTrainingSessionUseCase
import be.mauricedeke.shinkai.domain.usecase.GetShortcutsUseCase
import be.mauricedeke.shinkai.domain.usecase.ToggleShortcutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeUpcomingEvents: GetHomeUpcomingEventsUseCase,
    private val getNextTraining: GetNextTrainingSessionUseCase,
    private val getShortcuts: GetShortcutsUseCase,
    private val toggleShortcutUseCase: ToggleShortcutUseCase
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
            _uiState.update { it.copy(isEventsError = false, isTrainingError = false, isRefreshing = true) }
            val upcomingEvents = getHomeUpcomingEvents()
            val nextTrainingResult = getNextTraining()
            _uiState.update {
                it.copy(
                    upcomingEvents = upcomingEvents ?: emptyList(),
                    nextTraining = nextTrainingResult.getOrNull(),
                    isEventsError = upcomingEvents == null,
                    isTrainingError = nextTrainingResult.isFailure,
                    isRefreshing = false
                )
            }
        }
    }

    fun toggleShortcut(id: ShortcutId) {
        viewModelScope.launch {
            val currentIds = _uiState.value.shortcuts.map { it.name }
            val newIds = toggleShortcutUseCase(currentIds, id.name, MAX_SHORTCUTS)
            val newShortcuts = newIds.mapNotNull { runCatching { ShortcutId.valueOf(it) }.getOrNull() }
            _uiState.update { it.copy(shortcuts = newShortcuts) }
        }
    }
}
