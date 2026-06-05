package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.UserApi
import be.mauricedeke.shinkai.data.remote.dto.UserProfileDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserClient @Inject constructor(private val api: UserApi) {

    suspend fun getProfile(): Result<UserProfileDto> =
        runCatching { api.getProfile() }

    suspend fun updateProfile(profile: UserProfileDto): Result<UserProfileDto> =
        runCatching { api.updateProfile(profile) }
}
