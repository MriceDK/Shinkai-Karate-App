package be.mauricedeke.shinkai.data.remote.mapper

import be.mauricedeke.shinkai.data.remote.dto.KataDto
import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.domain.model.Kata

fun KataDto.toDomain() = Kata(
    id = id,
    name = name,
    belt = belt ?: "",
    beltColor = runCatching { BeltColor.valueOf(beltColor ?: "") }.getOrElse { BeltColor.YELLOW },
    description = description,
    moves = moves
)
