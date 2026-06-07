package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.data.local.datastore.AppDataStore
import be.mauricedeke.shinkai.data.remote.AuthTokenStore
import javax.inject.Inject

class ClearSessionUseCase @Inject constructor(
    private val appDataStore: AppDataStore,
    private val tokenStore: AuthTokenStore
) {
    suspend operator fun invoke() {
        appDataStore.clearAccessToken()
        tokenStore.accessToken = null
    }
}
