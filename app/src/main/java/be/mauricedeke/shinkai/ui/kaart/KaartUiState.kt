package be.mauricedeke.shinkai.ui.kaart

import be.mauricedeke.shinkai.domain.model.Event
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point

data class KaartUiState(
    val events: List<Event> = emptyList(),
    val userLocation: Point? = null,
    val selectedEvent: Event? = null,
    val routeGeometry: LineString? = null
)
