package be.mauricedeke.shinkai.ui.events

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.ui.components.ShinkaiCalendar
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    uiState: EventsUiState,
    onDateSelected: (LocalDate) -> Unit = {},
    onEventClick: (java.util.UUID) -> Unit = {},
    onManageEventsClick: () -> Unit = {},
    onRsvp: (eventId: java.util.UUID, attending: Boolean) -> Unit = { _, _ -> },
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val events = uiState.upcomingEvents
    val inboxEvents = uiState.inboxEvents
    val dateToEvents: Map<LocalDate, List<Event>> = uiState.allEvents
        .filter { it.localDate != null }
        .groupBy { it.localDate!! }
    val eventDates = dateToEvents.keys
    val today = LocalDate.now()
    val displayDate = uiState.selectedDate ?: today

    var bottomSheetEvents by remember { mutableStateOf<List<Event>>(emptyList()) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val density = LocalDensity.current
    val headerHeightPx = with(density) { 56.dp.toPx() }

    var calendarHeightPx by remember { mutableIntStateOf(0) }
    val animatable = remember { Animatable(2000f) }

    LaunchedEffect(calendarHeightPx) {
        if (calendarHeightPx > 0 && animatable.value >= 1500f) {
            animatable.snapTo((calendarHeightPx - headerHeightPx).coerceAtLeast(0f))
        }
    }

    if (bottomSheetEvents.isNotEmpty()) {
        ModalBottomSheet(
            onDismissRequest = { bottomSheetEvents = emptyList() },
            sheetState = sheetState
        ) {
            Text(
                "Events on this day",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
            bottomSheetEvents.forEachIndexed { index, event ->
                if (index > 0) HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                bottomSheetEvents = emptyList()
                                onEventClick(event.id)
                            }
                        }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.width(100.dp)) {
                        Text(
                            "${event.startTime} - ${event.endTime}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            event.localDate?.format(DateTimeFormatter.ofPattern("dd/MM"))
                                ?: event.date,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(event.title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                        if (event.location.isNotBlank())
                            Text(
                                event.location,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Toekomstige Events",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primaryContainer
            )
            if (uiState.isError) {
                Text(
                    "Kon events niet laden. Probeer opnieuw.",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp)
                )
            }
            Spacer(Modifier.height(8.dp))

            // Upcoming events list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 24.dp, vertical = 4.dp)
            ) {
                if (events.isEmpty()) {
                    Text(
                        "Geen aankomende events.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)
                    )
                } else {
                    events.forEach { event ->
                        val rsvp = uiState.rsvp[event.id]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clickable { onEventClick(event.id) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.width(110.dp)) {
                                Text(
                                    "${event.startTime} - ${event.endTime}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    event.localDate?.format(DateTimeFormatter.ofPattern("dd/MM"))
                                        ?: event.date,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(36.dp)
                                    .background(MaterialTheme.colorScheme.outlineVariant)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    event.title,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    event.location,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            }
                            Row {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (rsvp == false) MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.15f
                                            )
                                            else Color.Transparent
                                        )
                                        .clickable { onRsvp(event.id, false) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Ik kan niet",
                                        modifier = Modifier.size(16.dp),
                                        tint = if (rsvp == false) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (rsvp == true) MaterialTheme.colorScheme.primary
                                            else Color.Transparent
                                        )
                                        .clickable { onRsvp(event.id, true) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Ik kan",
                                        modifier = Modifier.size(16.dp),
                                        tint = if (rsvp == true) Color.White
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Inbox: upcoming events without an RSVP
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    "Inbox ( ${inboxEvents.size} )",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                Spacer(Modifier.height(6.dp))
                if (inboxEvents.isEmpty()) {
                    Text(
                        "Geen events zonder antwoord.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)
                    )
                } else {
                    inboxEvents.firstOrNull()?.let { event ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable { onEventClick(event.id) }
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                event.title,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "${event.startTime} - ${event.endTime}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Text(
                                        event.localDate?.format(DateTimeFormatter.ofPattern("dd/MM"))
                                            ?: event.date,
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                        fontSize = 12.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .width(1.dp)
                                        .height(36.dp)
                                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f))
                                )
                                Column {
                                    if (event.location.isNotBlank()) Text(
                                        event.location,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    if (event.city.isNotBlank()) Text(
                                        event.city,
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            val inboxRsvp = uiState.rsvp[event.id]
                            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth(0.85f)) {
                                SegmentedButton(
                                    selected = inboxRsvp == false,
                                    onClick = { onRsvp(event.id, false) },
                                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                                    icon = {
                                        if (inboxRsvp == false) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = null,
                                                modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                                            )
                                        }
                                    },
                                    colors = SegmentedButtonDefaults.colors(
                                        activeContainerColor = MaterialTheme.colorScheme.onPrimary.copy(
                                            alpha = 0.15f
                                        ),
                                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                                        activeBorderColor = MaterialTheme.colorScheme.onPrimary,
                                        inactiveContainerColor = MaterialTheme.colorScheme.primary,
                                        inactiveContentColor = MaterialTheme.colorScheme.onPrimary.copy(
                                            alpha = 0.8f
                                        ),
                                        inactiveBorderColor = MaterialTheme.colorScheme.onPrimary.copy(
                                            alpha = 0.5f
                                        ),
                                    )
                                ) {
                                    Text("Ik kan niet", fontSize = 13.sp)
                                }
                                SegmentedButton(
                                    selected = inboxRsvp == true,
                                    onClick = { onRsvp(event.id, true) },
                                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                                    colors = SegmentedButtonDefaults.colors(
                                        activeContainerColor = MaterialTheme.colorScheme.onPrimary,
                                        activeContentColor = MaterialTheme.colorScheme.primary,
                                        activeBorderColor = MaterialTheme.colorScheme.onPrimary,
                                        inactiveContainerColor = MaterialTheme.colorScheme.primary,
                                        inactiveContentColor = MaterialTheme.colorScheme.onPrimary.copy(
                                            alpha = 0.8f
                                        ),
                                        inactiveBorderColor = MaterialTheme.colorScheme.onPrimary.copy(
                                            alpha = 0.5f
                                        ),
                                    )
                                ) {
                                    Text("Ik kan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onManageEventsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(50)
            ) {
                Text("Beheer alle events", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(with(density) { headerHeightPx.toDp() } + 16.dp))
        }

        // Draggable calendar overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .onSizeChanged { size -> calendarHeightPx = size.height }
                .offset { IntOffset(0, animatable.value.roundToInt()) }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        val minimized = (calendarHeightPx - headerHeightPx).coerceAtLeast(0f)
                        scope.launch {
                            animatable.snapTo((animatable.value + delta).coerceIn(0f, minimized))
                        }
                    },
                    onDragStopped = {
                        val minimized = (calendarHeightPx - headerHeightPx).coerceAtLeast(0f)
                        val target = if (animatable.value < minimized / 2f) 0f else minimized
                        scope.launch {
                            animatable.animateTo(
                                target,
                                spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        }
                    }
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp), contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        displayDate.format(
                            DateTimeFormatter.ofPattern(
                                "EEE, MMM d",
                                Locale.ENGLISH
                            )
                        ),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                ShinkaiCalendar(
                    initialMonth = today.withDayOfMonth(1),
                    selectedDate = uiState.selectedDate,
                    today = today,
                    eventDates = eventDates,
                    onDateSelected = { date ->
                        onDateSelected(date)
                        val eventsOnDate = dateToEvents[date]
                        when {
                            eventsOnDate == null -> {}
                            eventsOnDate.size == 1 -> onEventClick(eventsOnDate.first().id)
                            else -> bottomSheetEvents = eventsOnDate
                        }
                    }
                )
            }
        }
    }
}

private val previewId1 = java.util.UUID.randomUUID()
private val previewId2 = java.util.UUID.randomUUID()
private val previewId3 = java.util.UUID.randomUUID()

private val previewUpcomingEvents = listOf(
    Event(
        id = previewId1, title = "Stage Naigairyu",
        startTime = "09:00", endTime = "12:00",
        date = "2026-06-15", location = "Sporthal Brugge", city = "Brugge",
        localDate = LocalDate.of(2026, 6, 15)
    ),
    Event(
        id = previewId2, title = "Examen Geel",
        startTime = "14:00", endTime = "16:00",
        date = "2026-06-22", location = "Dojo Gent", city = "Gent",
        localDate = LocalDate.of(2026, 6, 22)
    )
)

private val previewInboxEvent = Event(
    id = previewId3, title = "Zomerstage 2026",
    startTime = "09:00", endTime = "18:00",
    date = "2026-07-01", location = "Sporthal Kortrijk", city = "Kortrijk",
    localDate = LocalDate.of(2026, 7, 1)
)

@Preview(name = "Empty", showBackground = true, showSystemUi = true)
@Composable
fun EventsScreenEmptyPreview() {
    ShinkaikarateappTheme { EventsScreen(uiState = EventsUiState()) }
}

@Preview(name = "With upcoming events", showBackground = true, showSystemUi = true)
@Composable
fun EventsScreenWithEventsPreview() {
    ShinkaikarateappTheme {
        EventsScreen(
            uiState = EventsUiState(
                upcomingEvents = previewUpcomingEvents,
                rsvp = mapOf(previewId1 to true, previewId2 to null)
            )
        )
    }
}

@Preview(name = "With inbox event", showBackground = true, showSystemUi = true)
@Composable
fun EventsScreenWithInboxPreview() {
    ShinkaikarateappTheme {
        EventsScreen(
            uiState = EventsUiState(
                upcomingEvents = previewUpcomingEvents,
                inboxEvents = listOf(previewInboxEvent),
                rsvp = mapOf(previewId1 to false, previewId2 to true, previewId3 to null)
            )
        )
    }
}

@Preview(name = "Error", showBackground = true, showSystemUi = true)
@Composable
fun EventsScreenErrorPreview() {
    ShinkaikarateappTheme { EventsScreen(uiState = EventsUiState(isError = true)) }
}

@Preview(
    name = "Dark — with events",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun EventsScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) {
        EventsScreen(
            uiState = EventsUiState(
                upcomingEvents = previewUpcomingEvents,
                rsvp = mapOf(previewId1 to true)
            )
        )
    }
}
