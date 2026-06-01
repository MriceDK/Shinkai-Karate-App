package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.model.LexiconEntry
import be.mauricedeke.shinkai.domain.repository.LexiconRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LexiconRepositoryImpl @Inject constructor() : LexiconRepository {
    override suspend fun getLexiconEntries(): List<LexiconEntry>? = FakeDataSource.lexiconEntries
}
