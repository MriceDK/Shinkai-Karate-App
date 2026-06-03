package be.mauricedeke.shinkai.ui.profiel.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.NotificationSettings
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import be.mauricedeke.shinkai.ui.theme.ShinkaiRed
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

private val reminderOptions = listOf(
    15 to "15 minutes before",
    30 to "30 minutes before",
    60 to "1 hour before",
    120 to "2 hours before",
    1440 to "1 day before"
)

private val examReminderOptions = listOf(
    60 to "1 hour before",
    120 to "2 hours before",
    1440 to "1 day before",
    2880 to "2 days before",
    4320 to "3 days before",
    10080 to "1 week before",
    20160 to "2 weeks before"
)

@OptIn(ExperimentalMaterial3Api::class)
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
        Column(modifier = Modifier.fillMaxSize().padding(top = 64.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Text(
                    "Manage Notifications",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                NotifRow("Event Notifications", s.eventNotifications) {
                    onSettingsChanged(s.copy(eventNotifications = it))
                }
                AnimatedVisibility(visible = s.eventNotifications) {
                    ReminderPickerRow(
                        label = "Event Reminder",
                        subtitle = "Notify before an event starts",
                        selectedMinutes = s.reminderMinutesBefore,
                        onSelected = { onSettingsChanged(s.copy(reminderMinutesBefore = it)) }
                    )
                }
                NotifRow("Training Notifications", s.trainingNotifications) {
                    onSettingsChanged(s.copy(trainingNotifications = it))
                }
                AnimatedVisibility(visible = s.trainingNotifications) {
                    ReminderPickerRow(
                        label = "Training Reminder",
                        subtitle = "Notify before a training starts",
                        selectedMinutes = s.trainingReminderMinutesBefore,
                        onSelected = { onSettingsChanged(s.copy(trainingReminderMinutesBefore = it)) }
                    )
                }
                NotifRow(
                    "Change Notifications",
                    s.changeNotifications,
                    "Notifies you of changes to the techniques"
                ) { onSettingsChanged(s.copy(changeNotifications = it)) }
                NotifRow("Exam Notifications", s.examNotifications) {
                    onSettingsChanged(s.copy(examNotifications = it))
                }
                AnimatedVisibility(visible = s.examNotifications) {
                    ReminderPickerRow(
                        label = "Exam Reminder",
                        subtitle = "Notify before an exam starts",
                        selectedMinutes = s.examReminderMinutesBefore,
                        onSelected = { onSettingsChanged(s.copy(examReminderMinutesBefore = it)) },
                        options = examReminderOptions
                    )
                }
                NotifRow("Update Notifications", s.updateNotifications) {
                    onSettingsChanged(s.copy(updateNotifications = it))
                }
            }
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = ShinkaiRed),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 24.dp)
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
private fun NotifRow(
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
                checkedTrackColor = ShinkaiRed,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderPickerRow(
    label: String,
    subtitle: String,
    selectedMinutes: Int,
    onSelected: (Int) -> Unit,
    options: List<Pair<Int, String>> = reminderOptions
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.firstOrNull { it.first == selectedMinutes }?.second
        ?: "$selectedMinutes min before"
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.width(180.dp)
            ) {
                OutlinedTextField(
                    value = selectedLabel,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    options.forEach { (minutes, label) ->
                        DropdownMenuItem(
                            text = { Text(label, fontSize = 13.sp) },
                            onClick = { onSelected(minutes); expanded = false }
                        )
                    }
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationsScreenPreview() {
    ShinkaikarateappTheme { NotificationsScreen(uiState = NotificationsUiState()) }
}
