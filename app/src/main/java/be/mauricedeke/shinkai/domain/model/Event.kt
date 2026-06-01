package be.mauricedeke.shinkai.domain.model

import java.time.LocalDate

data class Event(
    val id: String = "",
    val title: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val date: String = "",
    val location: String = "",
    val city: String = "",
    val description: String = "",
    val isInbox: Boolean = false,
    val localDate: LocalDate? = null
)
