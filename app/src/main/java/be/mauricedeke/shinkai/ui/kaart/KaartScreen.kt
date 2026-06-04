package be.mauricedeke.shinkai.ui.kaart

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

@Composable
fun KaartScreen(
    uiState: KaartUiState,
    onNavigateToEvent: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    MapboxMap(
        modifier = modifier.fillMaxSize(),
        mapViewportState = rememberMapViewportState()
    )
}

@Preview(name = "Default — dojo location", showBackground = true, showSystemUi = true)
@Composable
fun KaartScreenPreview() {
    ShinkaikarateappTheme { KaartScreen(uiState = KaartUiState()) }
}

@Preview(name = "With event navigation button", showBackground = true, showSystemUi = true)
@Composable
fun KaartScreenWithEventPreview() {
    ShinkaikarateappTheme { KaartScreen(uiState = KaartUiState(showEventDetail = true)) }
}

@Preview(name = "Dark — with event navigation", showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun KaartScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) { KaartScreen(uiState = KaartUiState(showEventDetail = true)) }
}
