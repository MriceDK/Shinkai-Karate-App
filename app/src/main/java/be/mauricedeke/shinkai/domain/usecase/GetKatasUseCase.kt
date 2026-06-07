package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Kata
import be.mauricedeke.shinkai.domain.repository.KataRepository
import javax.inject.Inject

class GetKatasUseCase @Inject constructor(
    private val kataRepository: KataRepository
) {
    suspend operator fun invoke(): List<Kata>? = kataRepository.getKatas()
}
