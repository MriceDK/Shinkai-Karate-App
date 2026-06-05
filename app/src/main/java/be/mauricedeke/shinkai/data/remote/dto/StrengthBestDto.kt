package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StrengthBestDto(
    val type: String,
    val bestScore: Double
)
