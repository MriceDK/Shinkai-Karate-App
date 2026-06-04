package be.mauricedeke.shinkai.ui.kaart

import be.mauricedeke.shinkai.domain.model.Event
import com.mapbox.geojson.Point

data class KaartUiState(
    val showEventDetail: Boolean = false,
    val events: List<Event> = emptyList(),
    val userLocation: Point? = null
)
