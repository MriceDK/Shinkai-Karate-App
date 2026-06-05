package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.BeltDto
import be.mauricedeke.shinkai.data.remote.dto.BeltNoteDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface BeltApi {

    @GET("belts")
    suspend fun getBelts(): List<BeltDto>

    @GET("belts/{name}")
    suspend fun getBeltByName(@Path("name") name: String): BeltDto

    @GET("belts/{name}/notes")
    suspend fun getBeltNote(@Path("name") name: String): BeltNoteDto

    @PUT("belts/{name}/notes")
    suspend fun updateBeltNote(@Path("name") name: String, @Body body: BeltNoteDto): BeltNoteDto
}
