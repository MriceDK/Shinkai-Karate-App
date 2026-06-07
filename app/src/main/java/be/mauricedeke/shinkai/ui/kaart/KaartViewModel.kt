package be.mauricedeke.shinkai.ui.kaart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.remote.client.TrainingSessionClient
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.repository.EventRepository
import be.mauricedeke.shinkai.domain.repository.GeocodingRepository
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
    private val routeRepository: RouteRepository,
    private val eventRepository: EventRepository,
    private val trainingSessionClient: TrainingSessionClient,
    private val geocodingRepository: GeocodingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KaartUiState())
    val uiState: StateFlow<KaartUiState> = _uiState

    init {
        viewModelScope.launch {
            locationRepository.location.collect { point ->
                _uiState.update { it.copy(userLocation = point) }
            }
        }
        load()
    }

    fun load() {
        viewModelScope.launch {
            val events = eventRepository.getEvents() ?: emptyList()
            _uiState.update { it.copy(events = events) }
        }
        viewModelScope.launch {
            val sessions = trainingSessionClient.getTrainingSessions().getOrNull() ?: emptyList()
            val points = sessions.mapNotNull { dto ->
                if (dto.location.isBlank()) return@mapNotNull null
                val coords = geocodingRepository.geocode(dto.location) ?: return@mapNotNull null
                TrainingSessionPoint(
                    id = dto.id,
                    type = dto.type,
                    date = dto.date,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    location = dto.location,
                    lat = coords.first,
                    lng = coords.second
                )
            }
            _uiState.update { it.copy(trainingPoints = points) }
        }
    }

    fun onLocationStart() = locationRepository.start()
    fun onLocationStop() = locationRepository.stop()

    fun onEventSelected(event: Event?) {
        _uiState.update { it.copy(selectedEvent = event, routeGeometry = if (event != null) null else it.routeGeometry) }
    }

    fun onTrainingSelected(point: TrainingSessionPoint?) {
        _uiState.update { it.copy(selectedTrainingPoint = point) }
    }

    fun fetchRoute(destination: Point) {
        val origin = _uiState.value.userLocation ?: return
        viewModelScope.launch {
            val line = routeRepository.fetchRoute(origin, destination)
            _uiState.update { it.copy(routeGeometry = line) }
        }
    }
}
