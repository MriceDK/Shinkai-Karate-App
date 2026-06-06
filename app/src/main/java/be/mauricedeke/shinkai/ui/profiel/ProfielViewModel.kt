package be.mauricedeke.shinkai.ui.profiel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.local.datastore.AppDataStore
import be.mauricedeke.shinkai.data.remote.AuthTokenStore
import be.mauricedeke.shinkai.domain.usecase.ObserveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfielViewModel @Inject constructor(
    observeUserProfile: ObserveUserProfileUseCase,
    private val appDataStore: AppDataStore,
    private val tokenStore: AuthTokenStore
) : ViewModel() {

    val uiState = observeUserProfile()
        .map { ProfielUiState(userProfile = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfielUiState()
        )

    fun logout() {
        viewModelScope.launch {
            appDataStore.clearAccessToken()
            tokenStore.accessToken = null
        }
    }
}
