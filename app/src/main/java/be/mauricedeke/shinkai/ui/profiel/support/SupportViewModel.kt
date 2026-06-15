package be.mauricedeke.shinkai.ui.profiel.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.CreateSupportTicketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val createSupportTicket: CreateSupportTicketUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SupportUiState())
    val uiState: StateFlow<SupportUiState> = _uiState

    fun onSubjectChanged(value: String) {
        _uiState.update { it.copy(subject = value, errorMessage = null) }
    }

    fun onMessageChanged(value: String) {
        _uiState.update { it.copy(message = value, errorMessage = null) }
    }

    fun onSend() {
        val state = _uiState.value
        if (state.subject.isBlank() || state.message.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vul alle velden in.") }
            return
        }
        _uiState.update { it.copy(isSending = true, errorMessage = null) }
        viewModelScope.launch {
            createSupportTicket(state.subject.trim(), state.message.trim())
                .onSuccess { _uiState.update { it.copy(isSending = false, isSuccess = true) } }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isSending = false,
                            errorMessage = "Versturen mislukt. Probeer opnieuw."
                        )
                    }
                }
        }
    }

    fun onSuccessDismissed() {
        _uiState.update { SupportUiState() }
    }
}
