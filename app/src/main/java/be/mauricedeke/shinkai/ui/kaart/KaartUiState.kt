package be.mauricedeke.shinkai.ui.kaart

import be.mauricedeke.shinkai.domain.model.Event
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point

data class TrainingSessionPoint(
    val id: String,
    val type: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val location: String,
    val lat: Double,
    val lng: Double
)

data class KaartUiState(
    val events: List<Event> = emptyList(),
    val trainingPoints: List<TrainingSessionPoint> = emptyList(),
    val userLocation: Point? = null,
    val selectedEvent: Event? = null,
    val selectedTrainingPoint: TrainingSessionPoint? = null,
    val routeGeometry: LineString? = null,
    val showOnMap: Boolean = true
)
