package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.LexiconEntryDto
import retrofit2.http.GET

interface LexiconApi {

    @GET("lexicon")
    suspend fun getLexiconEntries(): List<LexiconEntryDto>
}
