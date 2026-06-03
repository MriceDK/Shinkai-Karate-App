package be.mauricedeke.shinkai.ui.profiel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetUserProfileUseCase
import be.mauricedeke.shinkai.domain.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfielViewModel @Inject constructor(
    private val getUserProfile: GetUserProfileUseCase,
    private val updateUserProfile: UpdateUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfielUiState())
    val uiState: StateFlow<ProfielUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(userProfile = getUserProfile()) }
        }
    }

    fun onProfilePictureSelected(uri: String) {
        viewModelScope.launch {
            val current = _uiState.value.userProfile
            val updated = (current ?: be.mauricedeke.shinkai.domain.model.UserProfile()).copy(profilePictureUri = uri)
            updateUserProfile(updated)
            _uiState.update { it.copy(userProfile = updated) }
        }
    }
}
