package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponseDto(
    val accessToken: String,
    val userId: String,
    val mustChangePassword: Boolean
)
