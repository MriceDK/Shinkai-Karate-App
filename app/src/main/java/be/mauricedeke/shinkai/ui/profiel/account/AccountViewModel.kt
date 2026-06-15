package be.mauricedeke.shinkai.ui.profiel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.remote.client.AuthClient
import be.mauricedeke.shinkai.domain.model.UserProfile
import be.mauricedeke.shinkai.domain.usecase.GetUserProfileUseCase
import be.mauricedeke.shinkai.domain.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getUserProfile: GetUserProfileUseCase,
    private val updateUserProfile: UpdateUserProfileUseCase,
    private val authClient: AuthClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val profile = getUserProfile()
            _uiState.update {
                it.copy(
                    userId = profile?.userId,
                    naam = profile?.name ?: "",
                    email = profile?.email ?: "",
                    belt = profile?.belt ?: "Yellow belt",
                    profilePictureUri = profile?.profilePictureUri,
                    isLoading = false
                )
            }
        }
    }

    fun onNaamChanged(naam: String) = _uiState.update { it.copy(naam = naam) }
    fun onEmailChanged(email: String) = _uiState.update { it.copy(email = email) }
    fun onCurrentPasswordChanged(pw: String) = _uiState.update { it.copy(currentPassword = pw) }
    fun onNewPasswordChanged(pw: String) = _uiState.update { it.copy(newPassword = pw) }
    fun onConfirmPasswordChanged(pw: String) = _uiState.update { it.copy(confirmPassword = pw) }
    fun onProfilePictureSelected(uri: String) {
        _uiState.update { it.copy(profilePictureUri = uri) }
        viewModelScope.launch {
            val s = _uiState.value
            updateUserProfile(
                UserProfile(
                    userId = s.userId,
                    name = s.naam,
                    email = s.email,
                    belt = s.belt,
                    profilePictureUri = uri
                )
            )
        }
    }

    fun onSave() {
        viewModelScope.launch {
            val s = _uiState.value
            updateUserProfile(
                UserProfile(
                    userId = s.userId,
                    name = s.naam,
                    email = s.email,
                    belt = s.belt,
                    profilePictureUri = s.profilePictureUri
                )
            )
        }
    }

    fun resetProfile() {
        _uiState.value = AccountUiState()
    }

    fun onSavePassword() {
        val s = _uiState.value
        if (s.currentPassword.isBlank() || s.newPassword.isBlank() || s.newPassword != s.confirmPassword) return
        viewModelScope.launch {
            authClient.changePassword(s.currentPassword, s.newPassword)
            _uiState.update {
                it.copy(
                    currentPassword = "",
                    newPassword = "",
                    confirmPassword = ""
                )
            }
        }
    }
}
