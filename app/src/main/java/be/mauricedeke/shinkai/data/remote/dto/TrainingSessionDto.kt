package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrainingSessionDto(
    val id: String,
    val type: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val sensei: String,
    val location: String
)
