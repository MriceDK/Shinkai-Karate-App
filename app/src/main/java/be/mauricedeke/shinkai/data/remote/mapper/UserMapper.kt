package be.mauricedeke.shinkai.data.remote.mapper

import be.mauricedeke.shinkai.data.remote.dto.UserProfileDto
import be.mauricedeke.shinkai.domain.model.UserProfile
import java.util.UUID

fun UserProfileDto.toDomain() = UserProfile(
    userId = runCatching { UUID.fromString(userId) }.getOrNull(),
    name = name,
    email = email,
    belt = belt,
    profilePictureUri = profilePictureUrl
)

fun UserProfile.toDto() = UserProfileDto(
    userId = userId?.toString() ?: "",
    name = name,
    email = email,
    belt = belt,
    profilePictureUrl = profilePictureUri
)
