package be.mauricedeke.shinkai.domain.repository

import kotlinx.coroutines.flow.Flow

interface MicrophoneRepository {
    val amplitude: Flow<Int>
    fun start()
    fun stop()
}
