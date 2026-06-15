package be.mauricedeke.shinkai.data.remote.mapper

import be.mauricedeke.shinkai.data.remote.dto.BeltDto
import be.mauricedeke.shinkai.data.remote.dto.TechniekDto
import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.domain.model.BeltProgram
import be.mauricedeke.shinkai.domain.model.ProgramSection
import be.mauricedeke.shinkai.domain.model.Techniek
import java.util.UUID

fun BeltDto.toDomain(notes: String = "") = Belt(
    name = name,
    beltColor = runCatching { BeltColor.valueOf(beltColor) }.getOrElse { BeltColor.YELLOW },
    pogramma = BeltProgram(sections = programme.sections.map {
        ProgramSection(
            it.title,
            it.items
        )
    }),
    technieken = technieken.map { it.toDomain() },
    notes = notes
)

fun TechniekDto.toDomain() = Techniek(
    id = UUID.randomUUID(),
    name = name,
    belt = belt ?: "",
    description = description,
    programma = programme ?: ""
)
