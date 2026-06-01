package be.mauricedeke.shinkai.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetEventsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetTrainingsByDateUseCase
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
    private val getTrainings: GetTrainingsByDateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            val events = getEvents()
            val todayTrainings = getTrainings(LocalDate.now())
            _uiState.update {
                it.copy(
                    upcomingEvents = events,
                    nextTraining = todayTrainings.firstOrNull()
                )
            }
        }
    }
}
