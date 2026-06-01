package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.UserProfile
import be.mauricedeke.shinkai.domain.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): UserProfile = userRepository.getUserProfile()
}
