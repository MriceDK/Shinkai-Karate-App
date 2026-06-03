package be.mauricedeke.shinkai.domain.model

import java.util.UUID

data class UserProfile(
    val userId: UUID? = null,
    val name: String = "",
    val email: String = "",
    val belt: String = "Yellow belt",
    val profilePictureUri: String? = null
)
