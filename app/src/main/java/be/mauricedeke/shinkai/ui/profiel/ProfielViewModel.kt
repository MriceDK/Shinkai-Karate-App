package be.mauricedeke.shinkai.ui.profiel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.local.datastore.AppDataStore
import be.mauricedeke.shinkai.data.remote.AuthTokenStore
import be.mauricedeke.shinkai.domain.usecase.ClearUserProfileUseCase
import be.mauricedeke.shinkai.domain.usecase.GetUserProfileUseCase
import be.mauricedeke.shinkai.domain.usecase.ObserveUserProfileUseCase
import be.mauricedeke.shinkai.messaging.AmqpNotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfielViewModel @Inject constructor(
    observeUserProfile: ObserveUserProfileUseCase,
    private val getUserProfile: GetUserProfileUseCase,
    private val clearUserProfile: ClearUserProfileUseCase,
    private val appDataStore: AppDataStore,
    private val tokenStore: AuthTokenStore,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val uiState = observeUserProfile()
        .map { ProfielUiState(userProfile = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfielUiState()
        )

    fun load() {
        viewModelScope.launch { getUserProfile() }
    }

    fun logout() {
        context.stopService(Intent(context, AmqpNotificationService::class.java))
        viewModelScope.launch {
            clearUserProfile()
            appDataStore.clearAccessToken()
            tokenStore.accessToken = null
        }
    }
}
