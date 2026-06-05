package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.AuthResponseDto
import be.mauricedeke.shinkai.data.remote.dto.ChangePasswordRequestDto
import be.mauricedeke.shinkai.data.remote.dto.LoginRequestDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestDto): AuthResponseDto

    @PUT("auth/password")
    suspend fun changePassword(@Body body: ChangePasswordRequestDto)
}
