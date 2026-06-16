package be.mauricedeke.shinkai.ui.kaart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.remote.client.TrainingSessionClient
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.repository.EventRepository
import be.mauricedeke.shinkai.domain.repository.LocationRepository
import be.mauricedeke.shinkai.domain.repository.RouteRepository
import be.mauricedeke.shinkai.domain.usecase.GetLocationSettingsUseCase
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class KaartViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val routeRepository: RouteRepository,
    private val eventRepository: EventRepository,
    private val trainingSessionClient: TrainingSessionClient,
    private val getLocationSettings: GetLocationSettingsUseCase,
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
            val showOnMap = getLocationSettings().showOnMap
            _uiState.update { it.copy(showOnMap = showOnMap) }
        }
        viewModelScope.launch {
            val today = LocalDate.now()
            val all = (eventRepository.getEvents() ?: emptyList())
                .filter { it.localDate != null }
            val upcomingEvents = all.filter { !it.localDate!!.isBefore(today) }
            val pastEvents = all.filter {
                it.localDate!!.isBefore(today) && !it.localDate.isBefore(today.minusDays(7))
            }
            _uiState.update { it.copy(upcomingEvents = upcomingEvents, pastEvents = pastEvents) }
        }
        viewModelScope.launch {
            val today = LocalDate.now()
            val sessions = trainingSessionClient.getTrainingSessions().getOrNull() ?: emptyList()
            val points = sessions.mapNotNull { dto ->
                val lat = dto.lat ?: return@mapNotNull null
                val lng = dto.lng ?: return@mapNotNull null
                TrainingSessionPoint(
                    id = dto.id,
                    type = dto.type,
                    date = dto.date,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    location = dto.location,
                    lat = lat,
                    lng = lng
                )
            }
            val upcoming = points.filter { point ->
                val date = runCatching { LocalDate.parse(point.date) }.getOrNull() ?: return@filter true
                !date.isBefore(today)
            }
            val past = points.filter { point ->
                val date = runCatching { LocalDate.parse(point.date) }.getOrNull() ?: return@filter false
                date.isBefore(today) && !date.isBefore(today.minusDays(3))
            }
            _uiState.update { it.copy(upcomingTrainingPoints = upcoming, pastTrainingPoints = past) }
        }
    }

    fun onLocationStart() = locationRepository.start()
    fun onLocationStop() = locationRepository.stop()

    fun onEventSelected(event: Event?) {
        _uiState.update {
            it.copy(
                selectedEvent = event,
                routeGeometry = if (event != null) null else it.routeGeometry
            )
        }
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
