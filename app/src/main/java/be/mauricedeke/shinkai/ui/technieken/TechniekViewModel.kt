package be.mauricedeke.shinkai.ui.technieken

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetBeltsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetKatasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechniekViewModel @Inject constructor(
    private val getBelts: GetBeltsUseCase,
    private val getKatas: GetKatasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TechniekUiState())
    val uiState: StateFlow<TechniekUiState> = _uiState

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isError = false, isRefreshing = true) }
            val belts = getBelts()
            _uiState.update {
                it.copy(
                    belts = belts ?: emptyList(),
                    isError = belts == null,
                    isRefreshing = false
                )
            }
        }
    }

    fun loadKatas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isKataError = false) }
            val katas = getKatas()
            _uiState.update {
                it.copy(
                    katas = katas ?: emptyList(),
                    isKataError = katas == null
                )
            }
        }
    }
}
