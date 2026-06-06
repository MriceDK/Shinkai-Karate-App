package be.mauricedeke.shinkai.data.remote.mapper

import be.mauricedeke.shinkai.data.remote.dto.TrainingDto
import be.mauricedeke.shinkai.domain.model.Training
import java.time.LocalDate
import java.util.UUID

fun TrainingDto.toDomain() = Training(
    id = UUID.fromString(id),
    type = type,
    startTime = startTime,
    endTime = endTime,
    date = runCatching { LocalDate.parse(date) }.getOrElse { LocalDate.now() },
    injuries = injuries,
    sensei = sensei,
    note = note
)
