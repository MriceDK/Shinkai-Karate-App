package be.mauricedeke.shinkai.ui.kaart

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
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
    onNavigateToEvent: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current
    val mapViewportState = rememberMapViewportState()
    var locationGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (isPreview) return@LaunchedEffect
        val fineGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            locationGranted = true
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(locationGranted) {
        if (locationGranted) {
            mapViewportState.transitionToFollowPuckState()
        }
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

        MapEffect(Unit) { mapView ->
            mapView.location.updateSettings {
                enabled = true
                locationPuck = createDefault2DPuck(withBearing = true)
                puckBearing = PuckBearing.COURSE
                pulsingEnabled = true
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
