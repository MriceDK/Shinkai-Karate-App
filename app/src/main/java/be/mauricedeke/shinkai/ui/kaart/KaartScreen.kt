package be.mauricedeke.shinkai.ui.kaart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import be.mauricedeke.shinkai.R
import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.ColorValue
import com.mapbox.maps.extension.compose.style.DoubleValue
import com.mapbox.maps.extension.compose.style.layers.generated.FillLayer
import com.mapbox.maps.extension.compose.style.layers.generated.LineLayer
import com.mapbox.maps.extension.compose.style.sources.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaartScreen(
    uiState: KaartUiState,
    locationPermissionGranted: Boolean = false,
    onRequestLocationPermission: () -> Unit = {},
    onLocationStart: () -> Unit = {},
    onLocationStop: () -> Unit = {},
    onEventSelected: (Event?) -> Unit = {},
    onViewEventDetails: (java.util.UUID) -> Unit = {},
    onTrainingSelected: (TrainingSessionPoint?) -> Unit = {},
    onViewTrainingDetails: (String) -> Unit = {},
    onFetchRoute: (Point) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isPreview = LocalInspectionMode.current
    val mapViewportState = rememberMapViewportState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (!isPreview && !locationPermissionGranted && uiState.showOnMap) {
            onRequestLocationPermission()
        }
    }

    LaunchedEffect(locationPermissionGranted, uiState.showOnMap) {
        if (locationPermissionGranted && uiState.showOnMap) {
            onLocationStart()
            mapViewportState.transitionToFollowPuckState()
        } else {
            onLocationStop()
        }
    }

    DisposableEffect(Unit) {
        onDispose { onLocationStop() }
    }

    val markerIcon = rememberIconImage(
        key = "event-marker",
        painter = painterResource(R.drawable.ic_map_marker)
    )

    MapboxMap(
        modifier = modifier.fillMaxSize(),
        mapViewportState = mapViewportState
    ) {
        MapEffect(locationPermissionGranted, uiState.showOnMap) { mapView ->
            mapView.location.updateSettings {
                enabled = locationPermissionGranted && uiState.showOnMap
                if (enabled) {
                    locationPuck = createDefault2DPuck(withBearing = true)
                    puckBearing = PuckBearing.COURSE
                    pulsingEnabled = true
                }
            }
        }

        val emptyCollection = """{"type":"FeatureCollection","features":[]}"""
        val routeSourceState = rememberGeoJsonSourceState {
            data = GeoJSONData(emptyCollection)
        }
        LaunchedEffect(uiState.routeGeometry) {
            routeSourceState.data = uiState.routeGeometry
                ?.let {
                    GeoJSONData(
                        FeatureCollection.fromFeature(Feature.fromGeometry(it)).toJson()
                    )
                }
                ?: GeoJSONData(emptyCollection)
        }
        LineLayer(sourceState = routeSourceState) {
            lineColor = ColorValue(Color(0xFF1A73E8))
            lineWidth = DoubleValue(5.0)
        }

        val geofenceSourceState = rememberGeoJsonSourceState()
        LaunchedEffect(uiState.events, uiState.trainingPoints) {
            val eventFeatures = uiState.events
                .filter { it.lat != null && it.lng != null }
                .map { event ->
                    Feature.fromGeometry(
                        Polygon.fromLngLats(listOf(geofenceCirclePoints(event.lat!!, event.lng!!)))
                    )
                }
            val trainingFeatures = uiState.trainingPoints.map { point ->
                Feature.fromGeometry(
                    Polygon.fromLngLats(listOf(geofenceCirclePoints(point.lat, point.lng)))
                )
            }
            geofenceSourceState.data = GeoJSONData(
                FeatureCollection.fromFeatures(eventFeatures + trainingFeatures).toJson()
            )
        }
        val geofenceFillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        val geofenceStrokeColor = MaterialTheme.colorScheme.primary
        FillLayer(sourceState = geofenceSourceState) {
            fillColor = ColorValue(geofenceFillColor)
        }
        LineLayer(sourceState = geofenceSourceState) {
            lineColor = ColorValue(geofenceStrokeColor)
            lineWidth = DoubleValue(2.0)
        }

        uiState.events.forEach { event ->
            if (event.lat != null && event.lng != null) {
                key(event.id) {
                    PointAnnotation(point = Point.fromLngLat(event.lng, event.lat)) {
                        iconImage = markerIcon
                        iconSize = 0.8
                        interactionsState.onClicked {
                            onEventSelected(event)
                            true
                        }
                    }
                }
            }
        }

        uiState.trainingPoints.forEach { point ->
            key("training-${point.id}") {
                PointAnnotation(point = Point.fromLngLat(point.lng, point.lat)) {
                    iconImage = markerIcon
                    iconSize = 0.8
                    interactionsState.onClicked {
                        onTrainingSelected(point)
                        true
                    }
                }
            }
        }
    }

    if (uiState.selectedEvent != null) {
        ModalBottomSheet(
            onDismissRequest = { onEventSelected(null) },
            sheetState = sheetState
        ) {
            EventDetailSheet(
                event = uiState.selectedEvent,
                locationEnabled = uiState.showOnMap,
                onViewDetails = {
                    onEventSelected(null)
                    onViewEventDetails(uiState.selectedEvent.id)
                },
                onNavigate = {
                    val lat = uiState.selectedEvent.lat
                    val lng = uiState.selectedEvent.lng
                    if (lat != null && lng != null) {
                        val destination = Point.fromLngLat(lng, lat)
                        onEventSelected(null)
                        onFetchRoute(destination)
                        scope.launch {
                            mapViewportState.flyTo(
                                CameraOptions.Builder()
                                    .center(destination)
                                    .zoom(14.0)
                                    .build()
                            )
                        }
                    }
                }
            )
        }
    }

    if (uiState.selectedTrainingPoint != null) {
        ModalBottomSheet(
            onDismissRequest = { onTrainingSelected(null) },
            sheetState = sheetState
        ) {
            TrainingSessionDetailSheet(
                point = uiState.selectedTrainingPoint,
                locationEnabled = uiState.showOnMap,
                onViewDetails = {
                    onTrainingSelected(null)
                    onViewTrainingDetails(uiState.selectedTrainingPoint.id)
                },
                onNavigate = {
                    val destination = Point.fromLngLat(
                        uiState.selectedTrainingPoint.lng,
                        uiState.selectedTrainingPoint.lat
                    )
                    onTrainingSelected(null)
                    onFetchRoute(destination)
                    scope.launch {
                        mapViewportState.flyTo(
                            CameraOptions.Builder()
                                .center(destination)
                                .zoom(14.0)
                                .build()
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun TrainingSessionDetailSheet(
    point: TrainingSessionPoint,
    locationEnabled: Boolean,
    onViewDetails: () -> Unit,
    onNavigate: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = point.type,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_schedule),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = point.date,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${point.startTime} – ${point.endTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (point.location.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = point.location,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onNavigate,
            enabled = locationEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Navigate")
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onViewDetails,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View details")
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun EventDetailSheet(
    event: Event,
    locationEnabled: Boolean,
    onViewDetails: () -> Unit,
    onNavigate: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = event.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_schedule),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = event.date,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${event.startTime} – ${event.endTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                if (event.location.isNotBlank()) {
                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = event.city,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (event.description.isNotBlank()) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onNavigate,
            enabled = locationEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Navigate")
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onViewDetails,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View details")
        }

        Spacer(Modifier.height(16.dp))
    }
}

private fun geofenceCirclePoints(
    lat: Double,
    lng: Double,
    radiusMeters: Double = 50.0,
    steps: Int = 64
): List<Point> {
    val earthRadius = 6_371_000.0
    val latRad = Math.toRadians(lat)
    return (0..steps).map { i ->
        val angle = Math.toRadians(i * 360.0 / steps)
        val dLat = Math.toDegrees(radiusMeters / earthRadius * cos(angle))
        val dLng = Math.toDegrees(radiusMeters / (earthRadius * cos(latRad)) * sin(angle))
        Point.fromLngLat(lng + dLng, lat + dLat)
    }
}

@Preview(name = "Default", showBackground = true, showSystemUi = true)
@Composable
fun KaartScreenPreview() {
    ShinkaikarateappTheme { KaartScreen(uiState = KaartUiState(events = FakeDataSource.events)) }
}

@Preview(
    name = "Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun KaartScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) { KaartScreen(uiState = KaartUiState(events = FakeDataSource.events)) }
}
