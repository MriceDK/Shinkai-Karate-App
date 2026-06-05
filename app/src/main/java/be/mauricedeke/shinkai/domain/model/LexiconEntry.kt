package be.mauricedeke.shinkai.domain.model

import java.util.UUID

data class LexiconEntry(
    val id: UUID = UUID.randomUUID(),
    val japaneseWord: String = "",
    val translation: String = "",
    val description: String = ""
)
