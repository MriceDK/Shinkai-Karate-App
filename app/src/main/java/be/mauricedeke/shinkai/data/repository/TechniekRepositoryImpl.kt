package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.model.Techniek
import be.mauricedeke.shinkai.domain.repository.TechniekRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TechniekRepositoryImpl @Inject constructor() : TechniekRepository {
    override suspend fun getBelts(): List<Belt> = FakeDataSource.belts
    override suspend fun getTechnieksByBelt(belt: String): List<Techniek> =
        FakeDataSource.belts.find { it.name == belt }?.technieken ?: emptyList()
}
