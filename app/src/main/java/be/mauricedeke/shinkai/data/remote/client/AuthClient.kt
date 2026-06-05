package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.AuthApi
import be.mauricedeke.shinkai.data.remote.dto.AuthResponseDto
import be.mauricedeke.shinkai.data.remote.dto.ChangePasswordRequestDto
import be.mauricedeke.shinkai.data.remote.dto.LoginRequestDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthClient @Inject constructor(private val api: AuthApi) {

    suspend fun login(email: String, password: String): Result<AuthResponseDto> =
        runCatching { api.login(LoginRequestDto(email, password)) }

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> =
        runCatching { api.changePassword(ChangePasswordRequestDto(currentPassword, newPassword)) }
}
