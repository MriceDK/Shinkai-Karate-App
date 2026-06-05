package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StrengthResultDto(
    val type: String,
    val score: Double,
    val beltColor: String,
    val unit: String
)
