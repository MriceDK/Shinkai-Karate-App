package be.mauricedeke.shinkai.ui.profiel.account

data class AccountUiState(
    val naam: String = "Maurice De Kegel",
    val email: String = "maurice.de.kegel@student.howest.be",
    val newPassword: String = "",
    val confirmPassword: String = ""
)
