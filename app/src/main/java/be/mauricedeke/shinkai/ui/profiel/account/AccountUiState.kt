package be.mauricedeke.shinkai.ui.profiel.account

import java.util.UUID

data class AccountUiState(
    val userId: UUID? = null,
    val naam: String = "",
    val email: String = "",
    val belt: String = "Yellow belt",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val profilePictureUri: String? = null,
    val isLoading: Boolean = true
)
