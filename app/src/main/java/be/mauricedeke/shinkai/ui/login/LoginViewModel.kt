package be.mauricedeke.shinkai.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.local.datastore.AppDataStore
import be.mauricedeke.shinkai.data.remote.AuthTokenStore
import be.mauricedeke.shinkai.data.remote.client.AuthClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authClient: AuthClient,
    private val tokenStore: AuthTokenStore,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(value: String) = _uiState.update { it.copy(email = value, errorMessage = null) }
    fun onPasswordChanged(value: String) = _uiState.update { it.copy(password = value, errorMessage = null) }

    fun onLoginClick() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vul je e-mail en wachtwoord in.") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            authClient.login(state.email.trim(), state.password)
                .onSuccess { response ->
                    tokenStore.accessToken = response.accessToken
                    appDataStore.setAccessToken(response.accessToken)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true,
                            mustChangePassword = response.mustChangePassword
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Ongeldige inloggegevens.")
                    }
                }
        }
    }

    fun onLoginHandled() = _uiState.update { it.copy(loginSuccess = false) }
}
