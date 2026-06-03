package be.mauricedeke.shinkai.ui.profiel.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val updateUserProfile: UpdateUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    init {
        viewModelScope.launch {
            val profile = getUserProfile()
            if (profile != null) {
                _uiState.update {
                    it.copy(naam = profile.name, email = profile.email, profilePictureUri = profile.profilePictureUri)
                }
            }
        }
    }

    fun onNaamChanged(naam: String) = _uiState.update { it.copy(naam = naam) }
    fun onEmailChanged(email: String) = _uiState.update { it.copy(email = email) }
    fun onNewPasswordChanged(pw: String) = _uiState.update { it.copy(newPassword = pw) }
    fun onConfirmPasswordChanged(pw: String) = _uiState.update { it.copy(confirmPassword = pw) }
    fun onProfilePictureSelected(uri: String) = _uiState.update { it.copy(profilePictureUri = uri) }

    fun onSave() {
        viewModelScope.launch {
            val s = _uiState.value
            updateUserProfile(UserProfile(name = s.naam, email = s.email, profilePictureUri = s.profilePictureUri))
        }
    }
}
