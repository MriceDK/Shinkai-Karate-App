package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.StrengthApi
import be.mauricedeke.shinkai.data.remote.dto.StrengthBestDto
import be.mauricedeke.shinkai.data.remote.dto.StrengthResultDto
import be.mauricedeke.shinkai.data.remote.dto.SubmitStrengthRequestDto
import be.mauricedeke.shinkai.data.remote.dto.UpdateStrengthRequestDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StrengthClient @Inject constructor(private val api: StrengthApi) {

    suspend fun getStrengthBests(): Result<List<StrengthBestDto>> =
        runCatching { api.getStrengthBests() }

    suspend fun updateStrengthBest(type: String, bestScore: Double): Result<StrengthBestDto> =
        runCatching { api.updateStrengthBest(type, UpdateStrengthRequestDto(bestScore)) }

    suspend fun submitPunchTest(score: Double): Result<StrengthResultDto> =
        runCatching { api.submitPunchTest(SubmitStrengthRequestDto(score)) }

    suspend fun submitKiaiTest(score: Double): Result<StrengthResultDto> =
        runCatching { api.submitKiaiTest(SubmitStrengthRequestDto(score)) }
}
