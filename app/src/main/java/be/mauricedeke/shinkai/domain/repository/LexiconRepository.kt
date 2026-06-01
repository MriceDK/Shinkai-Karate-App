package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.LexiconEntry

interface LexiconRepository {
    suspend fun getLexiconEntries(): List<LexiconEntry>?
}
