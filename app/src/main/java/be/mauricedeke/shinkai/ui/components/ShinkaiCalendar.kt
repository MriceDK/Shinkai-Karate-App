package be.mauricedeke.shinkai.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.R
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ShinkaiCalendar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    initialMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    selectedDate: LocalDate? = null,
    today: LocalDate = LocalDate.now(),
    eventDates: Set<LocalDate> = emptySet(),
    onDateSelected: (LocalDate) -> Unit = {},
    onClear: () -> Unit = {}
) {
    var currentMonth by remember { mutableStateOf(initialMonth) }
    var swipeDirection by remember { mutableIntStateOf(1) } // 1 = forward, -1 = backward
    var dragAccumulator by remember { mutableFloatStateOf(0f) }
    var swipeConsumed by remember { mutableStateOf(false) }
    val swipeThreshold = 50f
    val containerColor =
        if (backgroundColor == Color.Unspecified) MaterialTheme.colorScheme.primary else backgroundColor

    fun goNext() {
        swipeDirection = 1; currentMonth = currentMonth.plusMonths(1)
    }

    fun goPrev() {
        swipeDirection = -1; currentMonth = currentMonth.minusMonths(1)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Text(
                    text = currentMonth.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM yyyy",
                            Locale.ENGLISH
                        )
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = { goPrev() }, modifier = Modifier.size(32.dp)) {
                Icon(
                    painterResource(R.drawable.ic_chevron_left),
                    contentDescription = "Previous",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = { goNext() }, modifier = Modifier.size(32.dp)) {
                Icon(
                    painterResource(R.drawable.ic_chevron_right),
                    contentDescription = "Next",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        day,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        AnimatedContent(
            targetState = currentMonth,
            transitionSpec = {
                val dir = swipeDirection
                slideInHorizontally { if (dir > 0) it else -it } togetherWith
                        slideOutHorizontally { if (dir > 0) -it else it }
            },
            label = "month"
        ) { month ->
            val daysInMonth = month.lengthOfMonth()
            val firstDayOfWeek = month.dayOfWeek.value % 7

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { dragAccumulator = 0f; swipeConsumed = false },
                            onDragEnd = { dragAccumulator = 0f; swipeConsumed = false },
                            onDragCancel = { dragAccumulator = 0f; swipeConsumed = false },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                if (!swipeConsumed) {
                                    dragAccumulator += dragAmount
                                    when {
                                        dragAccumulator < -swipeThreshold -> {
                                            goNext(); swipeConsumed = true
                                        }

                                        dragAccumulator > swipeThreshold -> {
                                            goPrev(); swipeConsumed = true
                                        }
                                    }
                                }
                            }
                        )
                    }
            ) {
                for (row in 0 until 6) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (col in 0 until 7) {
                            val day = row * 7 + col - firstDayOfWeek + 1
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (day in 1..daysInMonth) {
                                    val date = month.withDayOfMonth(day)
                                    val isSelected = selectedDate == date
                                    val isToday = today == date
                                    val hasEvent = date in eventDates
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f)
                                            .clip(CircleShape)
                                            .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Transparent)
                                            .then(
                                                if (isToday && !isSelected) Modifier.border(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.onPrimary,
                                                    CircleShape
                                                )
                                                else Modifier
                                            )
                                            .clickable { onDateSelected(date) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = day.toString(),
                                            color = if (isSelected) containerColor else MaterialTheme.colorScheme.onPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                                        )
                                        if (hasEvent) {
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isSelected) containerColor else MaterialTheme.colorScheme.onPrimary)
                                                    .align(Alignment.BottomCenter)
                                                    .offset(y = (-2).dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "Today",
                color = containerColor,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .clickable {
                        swipeDirection = if (currentMonth.isAfter(today)) -1 else 1
                        currentMonth = today.withDayOfMonth(1)
                        onDateSelected(today)
                    }
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShinkaiCalendarPreview() {
    ShinkaikarateappTheme {
        ShinkaiCalendar(
            initialMonth = LocalDate.of(2025, 8, 1),
            selectedDate = LocalDate.of(2025, 8, 14),
            today = LocalDate.of(2025, 8, 5)
        )
    }
}
