package be.mauricedeke.shinkai.domain.repository

import kotlinx.coroutines.flow.Flow

interface AccelerometerRepository {
    val impact: Flow<Float>
    fun start()
    fun stop()
}
