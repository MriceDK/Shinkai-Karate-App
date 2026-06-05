package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserProfile(): UserProfile?
    fun observeUserProfile(): Flow<UserProfile?>
    suspend fun updateUserProfile(profile: UserProfile)
    suspend fun updatePassword(newPassword: String)
}
