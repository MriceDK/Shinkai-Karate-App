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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import be.mauricedeke.shinkai.ui.components.SettingsToggleRow
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
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val s = uiState.settings
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp)
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
                SettingsToggleRow("Event Notifications", s.eventNotifications) {
                    onSettingsChanged(s.copy(eventNotifications = it))
                }
                AnimatedVisibility(visible = s.eventNotifications) {
                    Column {
                        SettingsToggleRow("Event Reminders", s.eventReminderEnabled, "Notify before an event starts") {
                            onSettingsChanged(s.copy(eventReminderEnabled = it))
                        }
                        AnimatedVisibility(visible = s.eventReminderEnabled) {
                            ReminderPickerRow(
                                label = "Event Reminder Timing",
                                subtitle = "How long before the event",
                                selectedMinutes = s.reminderMinutesBefore,
                                onSelected = { onSettingsChanged(s.copy(reminderMinutesBefore = it)) }
                            )
                        }
                    }
                }
                SettingsToggleRow("Training Notifications", s.trainingNotifications) {
                    onSettingsChanged(s.copy(trainingNotifications = it))
                }
                AnimatedVisibility(visible = s.trainingNotifications) {
                    Column {
                        SettingsToggleRow("Training Reminders", s.trainingReminderEnabled, "Notify before a training starts") {
                            onSettingsChanged(s.copy(trainingReminderEnabled = it))
                        }
                        AnimatedVisibility(visible = s.trainingReminderEnabled) {
                            ReminderPickerRow(
                                label = "Training Reminder Timing",
                                subtitle = "How long before the training",
                                selectedMinutes = s.trainingReminderMinutesBefore,
                                onSelected = { onSettingsChanged(s.copy(trainingReminderMinutesBefore = it)) }
                            )
                        }
                    }
                }
                SettingsToggleRow(
                    "Change Notifications",
                    s.changeNotifications,
                    "Notifies you of changes to the techniques"
                ) { onSettingsChanged(s.copy(changeNotifications = it)) }
                SettingsToggleRow("Exam Notifications", s.examNotifications) {
                    onSettingsChanged(s.copy(examNotifications = it))
                }
                AnimatedVisibility(visible = s.examNotifications) {
                    Column {
                        SettingsToggleRow("Exam Reminders", s.examReminderEnabled, "Notify before an exam starts") {
                            onSettingsChanged(s.copy(examReminderEnabled = it))
                        }
                        AnimatedVisibility(visible = s.examReminderEnabled) {
                            ReminderPickerRow(
                                label = "Exam Reminder Timing",
                                subtitle = "How long before the exam",
                                selectedMinutes = s.examReminderMinutesBefore,
                                onSelected = { onSettingsChanged(s.copy(examReminderMinutesBefore = it)) },
                                options = examReminderOptions
                            )
                        }
                    }
                }
                SettingsToggleRow("Update Notifications", s.updateNotifications) {
                    onSettingsChanged(s.copy(updateNotifications = it))
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

@Preview(name = "All enabled", showBackground = true, showSystemUi = true)
@Composable
fun NotificationsScreenAllEnabledPreview() {
    ShinkaikarateappTheme { NotificationsScreen(uiState = NotificationsUiState()) }
}

@Preview(name = "Events disabled, training enabled", showBackground = true, showSystemUi = true)
@Composable
fun NotificationsScreenPartialPreview() {
    ShinkaikarateappTheme {
        NotificationsScreen(
            uiState = NotificationsUiState(
                settings = NotificationSettings(
                    eventNotifications = false,
                    eventReminderEnabled = false,
                    trainingNotifications = true,
                    trainingReminderEnabled = true,
                    examNotifications = false,
                    changeNotifications = true,
                    updateNotifications = false
                )
            )
        )
    }
}

@Preview(name = "Dark — all enabled", showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotificationsScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) { NotificationsScreen(uiState = NotificationsUiState()) }
}
