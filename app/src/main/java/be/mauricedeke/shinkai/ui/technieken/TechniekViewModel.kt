package be.mauricedeke.shinkai.ui.technieken

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetBeltsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechniekViewModel @Inject constructor(
    private val getBelts: GetBeltsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TechniekUiState())
    val uiState: StateFlow<TechniekUiState> = _uiState

    init {
        viewModelScope.launch {
            val belts = getBelts()
            _uiState.update {
                it.copy(
                    belts = belts ?: emptyList(),
                    isError = belts == null
                )
            }
        }
    }
}
