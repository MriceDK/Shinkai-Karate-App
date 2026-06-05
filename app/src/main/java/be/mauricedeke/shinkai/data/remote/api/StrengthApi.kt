package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.StrengthBestDto
import be.mauricedeke.shinkai.data.remote.dto.StrengthResultDto
import be.mauricedeke.shinkai.data.remote.dto.UpdateStrengthRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StrengthApi {

    @GET("strength-results")
    suspend fun getStrengthBests(): List<StrengthBestDto>

    @PUT("strength-results/{type}")
    suspend fun updateStrengthBest(
        @Path("type") type: String,
        @Body body: UpdateStrengthRequestDto
    ): StrengthBestDto

    @POST("strength-test/punch")
    suspend fun submitPunchTest(@Body body: UpdateStrengthRequestDto): StrengthResultDto

    @POST("strength-test/kiai")
    suspend fun submitKiaiTest(@Body body: UpdateStrengthRequestDto): StrengthResultDto
}
