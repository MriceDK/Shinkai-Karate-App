package be.mauricedeke.shinkai.ui.profiel.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import be.mauricedeke.shinkai.ui.components.RoundBackButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.NotificationSettings
import be.mauricedeke.shinkai.ui.theme.ShinkaiRed
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState,
    onSettingsChanged: (NotificationSettings) -> Unit = {},
    onSave: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val s = uiState.settings
    Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Text("Manage Notifications", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), color = MaterialTheme.colorScheme.onSurface)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        NotifRow("Event Notifications", s.eventNotifications) { onSettingsChanged(s.copy(eventNotifications = it)) }
        NotifRow("Training Notifications", s.trainingNotifications) { onSettingsChanged(s.copy(trainingNotifications = it)) }
        NotifRow("Change Notifications", s.changeNotifications, "Notifies you of changes to the techniques") { onSettingsChanged(s.copy(changeNotifications = it)) }
        NotifRow("Exam Notifications", s.examNotifications) { onSettingsChanged(s.copy(examNotifications = it)) }
        NotifRow("Update Notifications", s.updateNotifications) { onSettingsChanged(s.copy(updateNotifications = it)) }
        Spacer(Modifier.weight(1f))
        Button(onClick = onSave, colors = ButtonDefaults.buttonColors(containerColor = ShinkaiRed), shape = RoundedCornerShape(50), modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp).width(160.dp)) {
            Text("Save", fontSize = 16.sp)
        }
    }
    RoundBackButton(
        onClick = onBackClick,
        modifier = Modifier.align(Alignment.TopStart).padding(start = 16.dp, top = 8.dp)
    )
}
}

@Composable
private fun NotifRow(label: String, checked: Boolean, subtitle: String? = null, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
            if (subtitle != null) Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.onPrimary, checkedTrackColor = ShinkaiRed, uncheckedThumbColor = MaterialTheme.colorScheme.onSurface, uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant))
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationsScreenPreview() {
    ShinkaikarateappTheme { NotificationsScreen(uiState = NotificationsUiState()) }
}
