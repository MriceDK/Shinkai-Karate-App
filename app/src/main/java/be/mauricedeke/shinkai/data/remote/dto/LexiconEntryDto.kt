package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LexiconEntryDto(
    val id: String,
    val japaneseWord: String,
    val translation: String,
    val description: String
)
