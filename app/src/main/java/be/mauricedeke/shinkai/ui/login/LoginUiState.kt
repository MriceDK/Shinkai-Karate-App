package be.mauricedeke.shinkai.ui.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface LoginEvent {
    data class LoginSuccess(val mustChangePassword: Boolean) : LoginEvent
}
