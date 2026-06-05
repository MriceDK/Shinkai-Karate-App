package be.mauricedeke.shinkai.domain.model

import java.util.UUID

data class Techniek(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val belt: String = "",
    val description: String = "",
    val programma: String = ""
)
