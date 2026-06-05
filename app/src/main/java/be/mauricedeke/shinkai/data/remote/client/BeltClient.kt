package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.BeltApi
import be.mauricedeke.shinkai.data.remote.dto.BeltDto
import be.mauricedeke.shinkai.data.remote.dto.BeltNoteDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeltClient @Inject constructor(private val api: BeltApi) {

    suspend fun getBelts(): Result<List<BeltDto>> =
        runCatching { api.getBelts() }

    suspend fun getBeltByName(name: String): Result<BeltDto> =
        runCatching { api.getBeltByName(name) }

    suspend fun getBeltNote(name: String): Result<BeltNoteDto> =
        runCatching { api.getBeltNote(name) }

    suspend fun updateBeltNote(name: String, note: String): Result<BeltNoteDto> =
        runCatching { api.updateBeltNote(name, BeltNoteDto(note)) }
}
