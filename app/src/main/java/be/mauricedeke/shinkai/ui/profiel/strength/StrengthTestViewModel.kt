package be.mauricedeke.shinkai.ui.profiel.strength

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.FetchStrengthBestsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetStrengthHistoryUseCase
import be.mauricedeke.shinkai.domain.usecase.GetStrengthResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StrengthTestViewModel @Inject constructor(
    private val getStrengthResults: GetStrengthResultsUseCase,
    private val fetchStrengthBests: FetchStrengthBestsUseCase,
    private val getStrengthHistory: GetStrengthHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StrengthTestUiState())
    val uiState: StateFlow<StrengthTestUiState> = _uiState

    init {
        viewModelScope.launch {
            getStrengthResults().collect { results ->
                _uiState.update { it.copy(results = results) }
            }
        }
        viewModelScope.launch {
            getStrengthHistory().collect { history ->
                _uiState.update { it.copy(history = history) }
            }
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { fetchStrengthBests() }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
