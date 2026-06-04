package be.mauricedeke.shinkai.ui.profiel.location

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.LocationSettings
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

@Composable
fun LocationScreen(
    uiState: LocationUiState,
    onSettingsChanged: (LocationSettings) -> Unit = {},
    onSave: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val s = uiState.settings
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(top = 64.dp)) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text(
                "Manage Location Data Usage",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            LocationRow(
                "Use Location for training locations",
                s.useForTrainingLocations
            ) { onSettingsChanged(s.copy(useForTrainingLocations = it)) }
            LocationRow(
                "Use Location for improvements",
                s.useForImprovements
            ) { onSettingsChanged(s.copy(useForImprovements = it)) }
            LocationRow(
                "Use Location for tracking trainings",
                s.useForTrackingTrainings,
                "Automatically logs when you go to the dojo on a training day"
            ) { onSettingsChanged(s.copy(useForTrackingTrainings = it)) }
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

@Composable
private fun LocationRow(
    label: String,
    checked: Boolean,
    subtitle: String? = null,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
            if (subtitle != null) Text(
                subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
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
                    useForTrainingLocations = false,
                    useForImprovements = false,
                    useForTrackingTrainings = false
                )
            )
        )
    }
}

@Preview(name = "Dark — partial", showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LocationScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) {
        LocationScreen(
            uiState = LocationUiState(
                settings = LocationSettings(
                    useForTrainingLocations = true,
                    useForImprovements = false,
                    useForTrackingTrainings = true
                )
            )
        )
    }
}
