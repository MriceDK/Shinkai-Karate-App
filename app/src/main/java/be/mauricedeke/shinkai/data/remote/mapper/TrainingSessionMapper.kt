package be.mauricedeke.shinkai.data.remote.mapper

import be.mauricedeke.shinkai.data.remote.dto.TrainingSessionDto
import be.mauricedeke.shinkai.domain.model.TrainingSession
import java.time.LocalDate
import java.util.UUID

fun TrainingSessionDto.toDomain() = TrainingSession(
    id = runCatching { UUID.fromString(id) }.getOrElse { UUID.randomUUID() },
    type = type,
    startTime = startTime,
    endTime = endTime,
    date = runCatching { LocalDate.parse(date) }.getOrElse { LocalDate.now() },
    location = location,
    sensei = sensei,
    note = note
)
