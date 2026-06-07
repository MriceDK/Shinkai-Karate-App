package be.mauricedeke.shinkai.domain.model

import java.time.LocalDate
import java.util.UUID

data class Event(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val date: String = "",
    val location: String = "",
    val city: String = "",
    val description: String = "",
    val localDate: LocalDate? = null,
    val rsvp: Boolean? = null,
    val lat: Double? = null,
    val lng: Double? = null
)
