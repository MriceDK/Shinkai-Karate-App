package be.mauricedeke.shinkai.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.TrainingSession
import be.mauricedeke.shinkai.ui.components.EventRow
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {},
    onEventClick: (java.util.UUID) -> Unit = {},
    onTrainingClick: (java.util.UUID) -> Unit = {},
    onShortcutToggle: (ShortcutId) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    var showEditSheet by remember { mutableStateOf(false) }

    if (showEditSheet) {
        EditShortcutsSheet(
            selected = uiState.shortcuts,
            onToggle = onShortcutToggle,
            onDismiss = { showEditSheet = false }
        )
    }
    val upcomingEvents = uiState.upcomingEvents

    val nextTraining = uiState.nextTraining

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
                .padding(vertical = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            EventSection(title = "UPCOMING EVENTS") {
                if (uiState.isEventsError) {
                    Text(
                        "Kon events niet laden. Probeer opnieuw.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                } else if (upcomingEvents.isEmpty()) {
                    Text(
                        "Geen aankomende events.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                } else {
                    upcomingEvents.forEach { event ->
                        EventRow(
                            "${event.startTime} - ${event.endTime}",
                            event.localDate?.format(DateTimeFormatter.ofPattern("dd/MM"))
                                ?: event.date,
                            event.title,
                            event.location,
                            onClick = { onEventClick(event.id) })
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            EventSection(title = "VOLGENDE TRAINING") {
                when {
                    uiState.isTrainingError -> Text(
                        "Kon training niet laden. Probeer opnieuw.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )

                    nextTraining == null -> Text(
                        "Geen aankomende trainingen.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )

                    else -> EventRow(
                        "${nextTraining.startTime} - ${nextTraining.endTime}",
                        nextTraining.date.format(DateTimeFormatter.ofPattern("dd/MM")),
                        nextTraining.type,
                        nextTraining.location,
                        onClick = { onTrainingClick(nextTraining.id) },
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "SHORTCUTS",
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { showEditSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.height(32.dp)
                ) { Text("Edit", fontSize = 13.sp) }
            }

            Spacer(Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                val shortcuts = uiState.shortcuts
                shortcuts.chunked(2).forEach { rowItems ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowItems.forEachIndexed { index, shortcut ->
                            if (index > 0) Spacer(Modifier.width(8.dp))
                            ShortcutCard(
                                shortcut.label,
                                painterResource(shortcut.iconRes),
                                Modifier.weight(1f)
                            ) { onNavigate(shortcut.route) }
                        }
                        if (rowItems.size == 1) {
                            Spacer(Modifier.width(8.dp))
                            Spacer(Modifier.weight(1f))
                        }
                    }
                    if (shortcuts.chunked(2).last() != rowItems) Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun EventSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            title,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.primaryContainer
        )
        Spacer(Modifier.height(8.dp))
        content()
    }
}


@Composable
private fun ShortcutCard(
    label: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter,
                contentDescription = label,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview(name = "Empty", showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenEmptyPreview() {
    ShinkaikarateappTheme {
        HomeScreen(uiState = HomeUiState())
    }
}

@Preview(name = "With events and training", showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenWithDataPreview() {
    ShinkaikarateappTheme {
        HomeScreen(
            uiState = HomeUiState(
                upcomingEvents = listOf(
                    be.mauricedeke.shinkai.domain.model.Event(
                        id = java.util.UUID.randomUUID(), title = "Stage Naigairyu",
                        startTime = "09:00", endTime = "12:00",
                        date = "2026-06-15", location = "Sporthal Brugge"
                    ),
                    be.mauricedeke.shinkai.domain.model.Event(
                        id = java.util.UUID.randomUUID(), title = "Examen Geel",
                        startTime = "14:00", endTime = "16:00",
                        date = "2026-06-22", location = "Dojo Gent"
                    )
                ),
                nextTraining = TrainingSession(
                    type = "Technieken",
                    startTime = "20:00", endTime = "22:00",
                    date = java.time.LocalDate.now(),
                    sensei = "Sensei Kim"
                ),
                shortcuts = listOf(
                    ShortcutId.EVENTS,
                    ShortcutId.TECHNIEKEN,
                    ShortcutId.STRENGTH_TEST,
                    ShortcutId.TRAINING_HISTORY
                )
            )
        )
    }
}

@Preview(name = "Events error", showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenErrorPreview() {
    ShinkaikarateappTheme {
        HomeScreen(uiState = HomeUiState(isEventsError = true))
    }
}

@Preview(
    name = "Dark — with data",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) {
        HomeScreen(
            uiState = HomeUiState(
                upcomingEvents = listOf(
                    be.mauricedeke.shinkai.domain.model.Event(
                        id = java.util.UUID.randomUUID(), title = "Stage Naigairyu",
                        startTime = "09:00", endTime = "12:00",
                        date = "2026-06-15", location = "Sporthal Brugge"
                    )
                ),
                nextTraining = TrainingSession(
                    type = "Kata", startTime = "19:00", endTime = "21:00",
                    date = java.time.LocalDate.now()
                )
            )
        )
    }
}
