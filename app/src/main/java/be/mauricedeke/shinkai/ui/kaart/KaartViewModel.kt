package be.mauricedeke.shinkai.ui.kaart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KaartViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KaartUiState(events = FakeDataSource.events))
    val uiState: StateFlow<KaartUiState> = _uiState

    init {
        viewModelScope.launch {
            locationRepository.location.collect { point ->
                _uiState.update { it.copy(userLocation = point) }
            }
        }
    }

    fun onLocationStart() = locationRepository.start()
    fun onLocationStop() = locationRepository.stop()
}
