package be.mauricedeke.shinkai.ui.kaart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.repository.LocationRepository
import be.mauricedeke.shinkai.domain.repository.RouteRepository
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KaartViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val routeRepository: RouteRepository
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

    fun onEventSelected(event: Event?) {
        _uiState.update { it.copy(selectedEvent = event, routeGeometry = if (event != null) null else it.routeGeometry) }
    }

    fun fetchRoute(destination: Point) {
        val origin = _uiState.value.userLocation ?: return
        viewModelScope.launch {
            val line = routeRepository.fetchRoute(origin, destination)
            _uiState.update { it.copy(routeGeometry = line) }
        }
    }
}
