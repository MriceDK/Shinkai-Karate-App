package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.LexiconApi
import be.mauricedeke.shinkai.data.remote.dto.LexiconEntryDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LexiconClient @Inject constructor(private val api: LexiconApi) {

    suspend fun getLexiconEntries(): Result<List<LexiconEntryDto>> =
        runCatching { api.getLexiconEntries() }
}
