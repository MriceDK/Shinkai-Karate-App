package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(): UserProfile?
    suspend fun updateUserProfile(profile: UserProfile)
    suspend fun updatePassword(newPassword: String)
}
