package be.mauricedeke.shinkai.ui.profiel

import be.mauricedeke.shinkai.domain.model.UserProfile

data class ProfielUiState(
    val userProfile: UserProfile = UserProfile()
)
