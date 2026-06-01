package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.NoteRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(beltName: String, note: String) =
        noteRepository.saveNote(beltName, note)
}
