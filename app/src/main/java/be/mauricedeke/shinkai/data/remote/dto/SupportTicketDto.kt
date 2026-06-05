package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SupportTicketDto(
    val id: String,
    val subject: String,
    val message: String,
    val status: String,
    val createdAt: String
)
