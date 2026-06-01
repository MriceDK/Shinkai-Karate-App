package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(beltName: String): String = noteRepository.getNote(beltName)
}
