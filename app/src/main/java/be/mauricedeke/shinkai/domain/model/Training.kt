package be.mauricedeke.shinkai.domain.model

import java.time.LocalDate

data class Training(
    val id: String = "",
    val type: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val date: LocalDate = LocalDate.now(),
    val injuries: String = "Geen",
    val sensei: String = "Geen"
)
