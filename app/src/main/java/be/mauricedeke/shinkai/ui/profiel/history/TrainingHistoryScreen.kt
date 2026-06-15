package be.mauricedeke.shinkai.ui.profiel.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.R
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import be.mauricedeke.shinkai.ui.components.ShinkaiCalendar
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingHistoryScreen(
    uiState: TrainingHistoryUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
    onTrainingSelected: (java.util.UUID) -> Unit = {},
    onNotesChanged: (String) -> Unit = {},
    onNotesFocusLost: () -> Unit = {},
    onLogClick: () -> Unit = {},
    onLogSave: (type: String, startTime: String, endTime: String, sensei: String, injuries: String) -> Unit = { _, _, _, _, _ -> },
    onLogDismiss: () -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val displayTrainings = uiState.selectedTrainings

    if (uiState.showLogSheet) {
        LogTrainingSheet(
            date = uiState.selectedDate,
            initialType = uiState.logInitialType,
            initialStartTime = uiState.logInitialStartTime,
            initialEndTime = uiState.logInitialEndTime,
            onSave = onLogSave,
            onDismiss = onLogDismiss
        )
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                "TRAINING LOG",
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(12.dp))
            ShinkaiCalendar(
                modifier = Modifier.padding(horizontal = 4.dp),
                initialMonth = uiState.selectedDate.withDayOfMonth(1),
                selectedDate = uiState.selectedDate,
                today = LocalDate.now(),
                eventDates = uiState.trainingDates,
                onDateSelected = onDateSelected
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "TRAINING DATA",
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        uiState.selectedDate.format(
                            java.time.format.DateTimeFormatter.ofPattern(
                                "EEEE dd/MM/yyyy",
                                java.util.Locale("nl")
                            )
                        ),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = onLogClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.height(32.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
                ) {
                    Text("+ Log", fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            if (uiState.isError) {
                Text(
                    "Kon trainingen niet laden. Probeer opnieuw.",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (displayTrainings.isEmpty()) {
                Text(
                    "Geen trainingen op deze dag.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(displayTrainings) { training ->
                    TrainingCard(
                        training = training,
                        isSelected = uiState.selectedTrainingId == training.id,
                        onClick = { onTrainingSelected(training.id) },
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            val selectedTraining =
                displayTrainings.firstOrNull { it.id == uiState.selectedTrainingId }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (selectedTraining != null) 2.dp else 1.dp,
                        color = if (selectedTraining != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(2.dp)
            ) {
                Column {
                    Text(
                        if (selectedTraining != null) "Notes — ${selectedTraining.type}" else "Notes",
                        fontSize = 12.sp,
                        color = if (selectedTraining != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (selectedTraining != null) FontWeight.Medium else FontWeight.Normal,
                        modifier = Modifier.padding(start = 12.dp, top = 8.dp)
                    )
                    OutlinedTextField(
                        value = uiState.notes,
                        onValueChange = onNotesChanged,
                        enabled = selectedTraining != null,
                        placeholder = if (selectedTraining != null) null else {
                            { Text("Tik op een training om notes te bewerken", fontSize = 12.sp) }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .onFocusChanged { if (!it.isFocused) onNotesFocusLost() },
                        minLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.5f
                            )
                        )
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
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
private fun TrainingCard(
    training: be.mauricedeke.shinkai.domain.model.Training,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(172.dp)
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(12.dp)
                )
                else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.75f)
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    training.type,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                TrainingCardRow(
                    painterResource(R.drawable.ic_access_time),
                    "${training.startTime} – ${training.endTime}"
                )
                Spacer(Modifier.height(6.dp))
                TrainingCardRow(painterResource(R.drawable.ic_person), training.sensei)
                Spacer(Modifier.height(6.dp))
                TrainingCardRow(painterResource(R.drawable.ic_medical_services), training.injuries)
            }
        }
    }
}

@Composable
private fun TrainingCardRow(painter: Painter, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(6.dp))
        Text(
            value,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private val previewTrainingId1 = java.util.UUID.randomUUID()

private val previewTrainings = listOf(
    be.mauricedeke.shinkai.domain.model.Training(
        id = previewTrainingId1, type = "Technieken",
        startTime = "20:00", endTime = "22:00",
        date = LocalDate.of(2026, 6, 4),
        sensei = "Sensei Kim", injuries = "Geen"
    ),
    be.mauricedeke.shinkai.domain.model.Training(
        type = "Kata",
        startTime = "19:30", endTime = "21:30",
        date = LocalDate.of(2026, 6, 4),
        sensei = "Sensei Luc", injuries = "Lichte kniepijn"
    )
)

@Preview(name = "Empty day", showBackground = true, showSystemUi = true)
@Composable
fun TrainingHistoryEmptyPreview() {
    ShinkaikarateappTheme { TrainingHistoryScreen(uiState = TrainingHistoryUiState()) }
}

@Preview(name = "Day with trainings — one selected", showBackground = true, showSystemUi = true)
@Composable
fun TrainingHistoryWithTrainingsPreview() {
    ShinkaikarateappTheme {
        TrainingHistoryScreen(
            uiState = TrainingHistoryUiState(
                selectedDate = LocalDate.of(2026, 6, 4),
                selectedTrainings = previewTrainings,
                trainingDates = setOf(
                    LocalDate.of(2026, 6, 4),
                    LocalDate.of(2026, 6, 11),
                    LocalDate.of(2026, 6, 18)
                ),
                selectedTrainingId = previewTrainingId1,
                notes = "Goed gewerkt aan gyaku-zuki. Meer focus nodig op heupbeweging."
            )
        )
    }
}

@Preview(name = "Error state", showBackground = true, showSystemUi = true)
@Composable
fun TrainingHistoryErrorPreview() {
    ShinkaikarateappTheme {
        TrainingHistoryScreen(uiState = TrainingHistoryUiState(isError = true))
    }
}
