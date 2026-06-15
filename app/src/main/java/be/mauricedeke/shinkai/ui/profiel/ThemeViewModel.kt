package be.mauricedeke.shinkai.ui.profiel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.usecase.GetDarkThemeEnabledUseCase
import be.mauricedeke.shinkai.domain.usecase.UpdateDarkThemeEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ThemeUiState(
    val darkThemeEnabled: Boolean? = null
)

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val getDarkThemeEnabled: GetDarkThemeEnabledUseCase,
    private val updateDarkThemeEnabled: UpdateDarkThemeEnabledUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThemeUiState())
    val uiState: StateFlow<ThemeUiState> = _uiState

    init {
        viewModelScope.launch {
            val persistedDarkTheme = getDarkThemeEnabled()
            _uiState.update { current ->
                if (current.darkThemeEnabled == null) current.copy(darkThemeEnabled = persistedDarkTheme) else current
            }
        }
    }

    fun onDarkThemeToggle(enabled: Boolean) {
        _uiState.update { it.copy(darkThemeEnabled = enabled) }
        viewModelScope.launch {
            updateDarkThemeEnabled(enabled)
        }
    }
}
