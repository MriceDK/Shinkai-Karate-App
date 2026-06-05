package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {

    @GET("users/me")
    suspend fun getProfile(): UserProfileDto

    @PUT("users/me")
    suspend fun updateProfile(@Body body: UserProfileDto): UserProfileDto
}
