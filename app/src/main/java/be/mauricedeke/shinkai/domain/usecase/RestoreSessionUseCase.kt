package be.mauricedeke.shinkai.domain.usecase

import android.util.Base64
import be.mauricedeke.shinkai.data.local.datastore.AppDataStore
import be.mauricedeke.shinkai.data.remote.AuthTokenStore
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import javax.inject.Inject

class RestoreSessionUseCase @Inject constructor(
    private val appDataStore: AppDataStore,
    private val tokenStore: AuthTokenStore
) {
    suspend operator fun invoke(): Boolean {
        val token = appDataStore.accessToken.first()
        return if (token != null && isJwtValid(token)) {
            tokenStore.accessToken = token
            true
        } else {
            if (token != null) appDataStore.clearAccessToken()
            tokenStore.accessToken = null
            false
        }
    }

    private fun isJwtValid(token: String): Boolean = try {
        val payload = token.split(".")[1]
        val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val exp = JSONObject(String(decoded)).getLong("exp")
        System.currentTimeMillis() / 1000 < exp
    } catch (e: Exception) {
        false
    }
}
