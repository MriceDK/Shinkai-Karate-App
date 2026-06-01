package be.mauricedeke.shinkai.ui.profiel.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.model.LocationSettings
import be.mauricedeke.shinkai.domain.usecase.GetLocationSettingsUseCase
import be.mauricedeke.shinkai.domain.usecase.UpdateLocationSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getLocationSettings: GetLocationSettingsUseCase,
    private val updateLocationSettings: UpdateLocationSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(settings = getLocationSettings()) }
        }
    }

    fun onSettingsChanged(settings: LocationSettings) {
        _uiState.update { it.copy(settings = settings) }
    }

    fun onSave() {
        viewModelScope.launch {
            updateLocationSettings(_uiState.value.settings)
        }
    }
}
