package be.mauricedeke.shinkai.domain.repository

import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    val location: Flow<Point>
    fun start()
    fun stop()
}
