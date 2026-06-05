package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserProfileDto(
    val userId: String,
    val name: String,
    val email: String,
    val belt: String,
    val profilePictureUrl: String?
)
