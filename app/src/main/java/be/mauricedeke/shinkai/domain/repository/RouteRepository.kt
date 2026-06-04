package be.mauricedeke.shinkai.domain.repository

import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point

interface RouteRepository {
    suspend fun fetchRoute(origin: Point, destination: Point): LineString?
}
