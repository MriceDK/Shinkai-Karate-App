package be.mauricedeke.shinkai.ui.events.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEventsScreen(
    uiState: ManageEventsUiState,
    onRsvp: (eventId: java.util.UUID, attending: Boolean) -> Unit,
    onBackClick: () -> Unit,
    onEventClick: (java.util.UUID) -> Unit = {},
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(48.dp))
                Text(
                    "Beheer Events",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Beheer je aanwezigheid voor alle geplande events.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(24.dp))

                if (uiState.isError) {
                    Spacer(Modifier.height(48.dp))
                    Text(
                        "Kon events niet laden. Probeer opnieuw.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (uiState.events.isEmpty()) {
                    Spacer(Modifier.height(48.dp))
                    Text(
                        "Geen events gevonden.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    uiState.events.forEachIndexed { index, event ->
                        if (index > 0) HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        EventManageRow(
                            event = event,
                            rsvp = uiState.rsvp[event.id],
                            onRsvp = { attending -> onRsvp(event.id, attending) },
                            onClick = { onEventClick(event.id) }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            RoundBackButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 8.dp)
            )
        }
    } // PullToRefreshBox
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventManageRow(
    event: Event,
    rsvp: Boolean?,
    onRsvp: (Boolean) -> Unit,
    onClick: () -> Unit = {}
) {
    val isUpcoming = event.localDate == null || !event.localDate.isBefore(LocalDate.now())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (!isUpcoming) Modifier.background(
                    Color(MaterialTheme.colorScheme.primary.value).copy(
                        alpha = 0.20f
                    ), RoundedCornerShape(12.dp)
                )
                else Modifier
            )
            .padding(vertical = 12.dp)
            .then(
                if (!isUpcoming) Modifier.padding(horizontal = 12.dp)
                else Modifier
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .clickable(onClick = onClick)) {
                Text(
                    event.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = if (isUpcoming) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    "${event.date}  •  ${event.startTime} – ${event.endTime}",
                    fontSize = 12.sp,
                    color = if (isUpcoming) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                )
                if (event.city.isNotBlank()) {
                    Text(
                        event.city,
                        fontSize = 12.sp,
                        color = if (isUpcoming) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    )
                }
            }
            if (!isUpcoming) {
                Text(
                    "Voorbij",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.65f),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
        if (isUpcoming) {
            Spacer(Modifier.height(8.dp))
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = rsvp == false,
                    onClick = { onRsvp(false) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    icon = {
                        if (rsvp == false) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                            )
                        }
                    },
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        activeContentColor = MaterialTheme.colorScheme.primary,
                        activeBorderColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text("Ik kan niet", fontSize = 13.sp)
                }
                SegmentedButton(
                    selected = rsvp == true,
                    onClick = { onRsvp(true) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = Color.White,
                        activeBorderColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text("Ik kan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

private val previewEventId1 = java.util.UUID.randomUUID()
private val previewEventId2 = java.util.UUID.randomUUID()
private val previewEventId3 = java.util.UUID.randomUUID()
private val previewEventId4 = java.util.UUID.randomUUID()

private val previewEvents = listOf(
    Event(
        id = previewEventId1,
        title = "Kumite training",
        date = "15 jun 2026",
        startTime = "19:00",
        endTime = "21:00",
        city = "Gent",
        localDate = LocalDate.now().plusDays(12)
    ),
    Event(
        id = previewEventId2,
        title = "Kata competitie",
        date = "22 jun 2026",
        startTime = "10:00",
        endTime = "17:00",
        city = "Brugge",
        localDate = LocalDate.now().plusDays(19)
    ),
    Event(
        id = previewEventId3,
        title = "Zomerstage",
        date = "1 jul 2026",
        startTime = "09:00",
        endTime = "18:00",
        city = "Kortrijk",
        localDate = LocalDate.now().plusDays(28)
    ),
    Event(
        id = previewEventId4,
        title = "Voorjaarsexamen",
        date = "4 mei 2026",
        startTime = "13:00",
        endTime = "16:00",
        city = "Gent",
        localDate = LocalDate.now().minusDays(24)
    )
)

@Preview(showBackground = true, showSystemUi = true, name = "With events")
@Composable
private fun ManageEventsScreenPreview() {
    ShinkaikarateappTheme {
        ManageEventsScreen(
            uiState = ManageEventsUiState(
                events = previewEvents,
                rsvp = mapOf(
                    previewEventId1 to true,
                    previewEventId2 to false,
                    previewEventId4 to true
                ),
                isError = false
            ),
            onRsvp = { _, _ -> },
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Empty")
@Composable
private fun ManageEventsScreenEmptyPreview() {
    ShinkaikarateappTheme {
        ManageEventsScreen(
            uiState = ManageEventsUiState(events = emptyList(), rsvp = emptyMap(), isError = false),
            onRsvp = { _, _ -> },
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Error")
@Composable
private fun ManageEventsScreenErrorPreview() {
    ShinkaikarateappTheme {
        ManageEventsScreen(
            uiState = ManageEventsUiState(events = emptyList(), rsvp = emptyMap(), isError = true),
            onRsvp = { _, _ -> },
            onBackClick = {}
        )
    }
}