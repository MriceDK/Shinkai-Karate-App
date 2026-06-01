package be.mauricedeke.shinkai.ui.profiel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfielViewModel @Inject constructor(
    private val getUserProfile: GetUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfielUiState())
    val uiState: StateFlow<ProfielUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(userProfile = getUserProfile()) }
        }
    }
}
