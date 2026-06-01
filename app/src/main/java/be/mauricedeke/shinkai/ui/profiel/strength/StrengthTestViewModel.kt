package be.mauricedeke.shinkai.ui.profiel.strength

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetStrengthResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StrengthTestViewModel @Inject constructor(
    private val getStrengthResults: GetStrengthResultsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StrengthTestUiState())
    val uiState: StateFlow<StrengthTestUiState> = _uiState

    init {
        viewModelScope.launch {
            val results = getStrengthResults()
            _uiState.update { it.copy(results = results ?: emptyList(), isError = results == null) }
        }
    }
}
