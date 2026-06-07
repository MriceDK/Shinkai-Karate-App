package be.mauricedeke.shinkai.ui.profiel.support

data class SupportUiState(
    val subject: String = "",
    val message: String = "",
    val isSending: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
