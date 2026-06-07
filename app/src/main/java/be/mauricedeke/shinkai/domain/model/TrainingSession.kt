package be.mauricedeke.shinkai.domain.model

import java.time.LocalDate
import java.util.UUID

data class TrainingSession(
    val id: UUID = UUID.randomUUID(),
    val type: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val date: LocalDate = LocalDate.now(),
    val location: String = "",
    val sensei: String? = null,
    val note: String? = null
)
