package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.LexiconEntry
import be.mauricedeke.shinkai.domain.repository.LexiconRepository
import javax.inject.Inject

class GetLexiconEntriesUseCase @Inject constructor(
    private val lexiconRepository: LexiconRepository
) {
    suspend operator fun invoke(): List<LexiconEntry> = lexiconRepository.getLexiconEntries()
}
