package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.model.Techniek

interface TechniekRepository {
    suspend fun getBelts(): List<Belt>
    suspend fun getTechnieksByBelt(belt: String): List<Techniek>
}
