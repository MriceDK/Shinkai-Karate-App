package be.mauricedeke.shinkai.domain.model

import java.time.LocalDate
import java.util.UUID

data class Training(
    val id: UUID = UUID.randomUUID(),
    val type: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val date: LocalDate = LocalDate.now(),
    val injuries: String = "Geen",
    val sensei: String = "Geen",
    val note: String = ""
)
