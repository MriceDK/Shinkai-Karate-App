package be.mauricedeke.shinkai.data.remote.mapper

import be.mauricedeke.shinkai.data.remote.dto.EventDto
import be.mauricedeke.shinkai.domain.model.Event
import java.time.LocalDate
import java.util.UUID

fun EventDto.toDomain() = Event(
    id = UUID.fromString(id),
    title = title,
    startTime = startTime,
    endTime = endTime,
    date = date,
    location = location,
    city = city,
    description = description,
    localDate = runCatching { LocalDate.parse(localDate) }.getOrNull(),
    rsvp = rsvp,
    lat = lat,
    lng = lng
)
