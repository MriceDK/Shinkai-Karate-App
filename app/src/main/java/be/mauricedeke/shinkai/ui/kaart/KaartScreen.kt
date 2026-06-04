package be.mauricedeke.shinkai.ui.kaart

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import be.mauricedeke.shinkai.R
import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.ColorValue
import com.mapbox.maps.extension.compose.style.DoubleValue
import com.mapbox.maps.extension.compose.style.layers.generated.FillLayer
import com.mapbox.maps.extension.compose.style.sources.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun KaartScreen(
    uiState: KaartUiState,
    locationPermissionGranted: Boolean = false,
    onRequestLocationPermission: () -> Unit = {},
    onLocationStart: () -> Unit = {},
    onLocationStop: () -> Unit = {},
    onNavigateToEvent: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isPreview = LocalInspectionMode.current
    val mapViewportState = rememberMapViewportState()

    LaunchedEffect(Unit) {
        if (!isPreview && !locationPermissionGranted) {
            onRequestLocationPermission()
        }
    }

    LaunchedEffect(locationPermissionGranted) {
        if (locationPermissionGranted) {
            onLocationStart()
            mapViewportState.transitionToFollowPuckState()
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
        FillLayer(
            sourceState = rememberGeoJsonSourceState {
                data = GeoJSONData(FakeDataSource.dojoZones)
            },
        ) {
            fillColor = ColorValue(Color.Red.copy(alpha = 0.3f))
            fillOpacity = DoubleValue(0.3)
        }

        MapEffect(locationPermissionGranted) { mapView ->
            if (locationPermissionGranted) {
                mapView.location.updateSettings {
                    enabled = true
                    locationPuck = createDefault2DPuck(withBearing = true)
                    puckBearing = PuckBearing.COURSE
                    pulsingEnabled = true
                }
            }
        }

        uiState.events.forEach { event ->
            if (event.lat != null && event.lng != null) {
                PointAnnotation(point = Point.fromLngLat(event.lng, event.lat)) {
                    iconImage = markerIcon
                    iconSize = 0.8
                }
            }
        }
    }
}

@Preview(name = "Default — dojo location", showBackground = true, showSystemUi = true)
@Composable
fun KaartScreenPreview() {
    ShinkaikarateappTheme { KaartScreen(uiState = KaartUiState(events = FakeDataSource.events)) }
}

@Preview(name = "With event navigation button", showBackground = true, showSystemUi = true)
@Composable
fun KaartScreenWithEventPreview() {
    ShinkaikarateappTheme { KaartScreen(uiState = KaartUiState(showEventDetail = true, events = FakeDataSource.events)) }
}

@Preview(name = "Dark — with event navigation", showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun KaartScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) { KaartScreen(uiState = KaartUiState(showEventDetail = true, events = FakeDataSource.events)) }
}
