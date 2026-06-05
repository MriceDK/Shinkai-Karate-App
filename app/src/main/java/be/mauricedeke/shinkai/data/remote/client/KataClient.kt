package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.KataApi
import be.mauricedeke.shinkai.data.remote.dto.KataDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KataClient @Inject constructor(private val api: KataApi) {

    suspend fun getKatas(): Result<List<KataDto>> =
        runCatching { api.getKatas() }
}
