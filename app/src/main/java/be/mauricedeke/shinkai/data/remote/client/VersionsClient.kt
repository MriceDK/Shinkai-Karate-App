package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.VersionsApi
import be.mauricedeke.shinkai.data.remote.dto.VersionsDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VersionsClient @Inject constructor(private val api: VersionsApi) {

    suspend fun getVersions(): Result<VersionsDto> =
        runCatching { api.getVersions() }
}
