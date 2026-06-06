package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.VersionsDto
import retrofit2.http.GET

interface VersionsApi {

    @GET("versions")
    suspend fun getVersions(): VersionsDto
}
