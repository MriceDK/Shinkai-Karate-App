package be.mauricedeke.shinkai.data.remote.mapper

import be.mauricedeke.shinkai.data.remote.dto.TrainingSessionDto
import be.mauricedeke.shinkai.domain.model.Training
import java.time.LocalDate
import java.util.UUID

fun TrainingSessionDto.toDomain() = Training(
    id = runCatching { UUID.fromString(id) }.getOrElse { UUID.randomUUID() },
    type = type,
    startTime = startTime,
    endTime = endTime,
    date = runCatching { LocalDate.parse(date) }.getOrElse { LocalDate.now() },
    sensei = sensei
)
