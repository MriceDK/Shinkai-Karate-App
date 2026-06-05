package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.KataDto
import retrofit2.http.GET

interface KataApi {

    @GET("katas")
    suspend fun getKatas(): List<KataDto>
}
