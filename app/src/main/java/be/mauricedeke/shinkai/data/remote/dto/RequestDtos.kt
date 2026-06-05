package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestDto(
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class RsvpRequestDto(
    val attending: Boolean
)

@JsonClass(generateAdapter = true)
data class CreateTrainingRequestDto(
    val type: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val injuries: String,
    val sensei: String
)

@JsonClass(generateAdapter = true)
data class NoteRequestDto(
    val note: String
)

@JsonClass(generateAdapter = true)
data class UpdateStrengthRequestDto(
    val bestScore: Double
)

@JsonClass(generateAdapter = true)
data class CreateSupportTicketRequestDto(
    val subject: String,
    val message: String
)

@JsonClass(generateAdapter = true)
data class ChangePasswordRequestDto(
    val currentPassword: String,
    val newPassword: String
)
