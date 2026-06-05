package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.UserRepository
import javax.inject.Inject

class ObserveUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() = userRepository.observeUserProfile()
}
