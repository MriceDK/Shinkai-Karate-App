package be.mauricedeke.shinkai.data.remote.mapper

import be.mauricedeke.shinkai.data.remote.dto.LexiconEntryDto
import be.mauricedeke.shinkai.domain.model.LexiconEntry
import java.util.UUID

fun LexiconEntryDto.toDomain() = LexiconEntry(
    id = runCatching { UUID.fromString(id) }.getOrElse { UUID.randomUUID() },
    japaneseWord = japaneseWord,
    translation = translation,
    description = description
)
