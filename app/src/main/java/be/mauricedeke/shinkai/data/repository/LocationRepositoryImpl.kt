package be.mauricedeke.shinkai.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import be.mauricedeke.shinkai.domain.repository.LocationRepository
import com.mapbox.geojson.Point
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : LocationRepository {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val _location = MutableStateFlow<Point?>(null)
    override val location: Flow<Point> = _location.filterNotNull()

    private val listener = LocationListener { loc: Location ->
        _location.value = Point.fromLngLat(loc.longitude, loc.latitude)
    }

    @SuppressLint("MissingPermission")
    override fun start() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.FUSED_PROVIDER,
                1000L,
                1f,
                listener,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) { /* permission not granted */
        }
    }

    override fun stop() {
        locationManager.removeUpdates(listener)
    }
}
