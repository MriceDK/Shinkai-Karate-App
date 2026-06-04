package be.mauricedeke.shinkai.ui.kaart

import androidx.lifecycle.ViewModel
import be.mauricedeke.shinkai.data.fake.FakeDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class KaartViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(KaartUiState(events = FakeDataSource.events))
    val uiState: StateFlow<KaartUiState> = _uiState
}
