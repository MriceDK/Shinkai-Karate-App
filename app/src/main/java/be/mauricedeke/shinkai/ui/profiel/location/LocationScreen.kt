package be.mauricedeke.shinkai.ui.profiel.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.LocationSettings
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import be.mauricedeke.shinkai.ui.components.SettingsToggleRow
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

@Composable
fun LocationScreen(
    uiState: LocationUiState,
    locationPermissionGranted: Boolean = true,
    onGrantLocationPermission: () -> Unit = {},
    onSettingsChanged: (LocationSettings) -> Unit = {},
    onSave: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val s = uiState.settings
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp)) {
            if (!locationPermissionGranted && (s.showOnMap || s.useForGeofencing)) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Location permission required",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                "Grant permission so these settings take effect",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                        TextButton(onClick = onGrantLocationPermission) {
                            Text(
                                "Grant",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text(
                "Manage Location Data Usage",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SettingsToggleRow(
                label = "Show my location on the map",
                checked = s.showOnMap,
                subtitle = "Displays your live position on the Kaart screen"
            ) { onSettingsChanged(s.copy(showOnMap = it)) }
            SettingsToggleRow(
                label = "Use location for geofencing",
                checked = s.useForGeofencing,
                subtitle = "Automatically prompts you to log attendance when you arrive at an event"
            ) { onSettingsChanged(s.copy(useForGeofencing = it)) }
            SettingsToggleRow(
                label = "Use location for global statistics",
                checked = s.useForStatistics,
                subtitle = "Contributes anonymous location data to club-wide training statistics"
            ) { onSettingsChanged(s.copy(useForStatistics = it)) }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp)
                    .width(160.dp)
            ) {
                Text("Save", fontSize = 16.sp)
            }
        }
        RoundBackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 8.dp)
        )
    }
}

@Preview(name = "All enabled", showBackground = true, showSystemUi = true)
@Composable
fun LocationScreenAllEnabledPreview() {
    ShinkaikarateappTheme { LocationScreen(uiState = LocationUiState()) }
}

@Preview(name = "All disabled", showBackground = true, showSystemUi = true)
@Composable
fun LocationScreenAllDisabledPreview() {
    ShinkaikarateappTheme {
        LocationScreen(
            uiState = LocationUiState(
                settings = LocationSettings(
                    showOnMap = false,
                    useForGeofencing = false,
                    useForStatistics = false
                )
            )
        )
    }
}

@Preview(
    name = "Dark — partial",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LocationScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) {
        LocationScreen(
            uiState = LocationUiState(
                settings = LocationSettings(
                    showOnMap = true,
                    useForGeofencing = false,
                    useForStatistics = true
                )
            )
        )
    }
}
