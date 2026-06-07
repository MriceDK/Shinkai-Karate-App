package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.Kata

interface KataRepository {
    suspend fun getKatas(): List<Kata>?
}
