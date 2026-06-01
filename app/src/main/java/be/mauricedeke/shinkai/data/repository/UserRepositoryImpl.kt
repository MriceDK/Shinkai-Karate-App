package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.model.UserProfile
import be.mauricedeke.shinkai.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    private var profile: UserProfile? = FakeDataSource.userProfile

    override suspend fun getUserProfile(): UserProfile? = profile
    override suspend fun updateUserProfile(profile: UserProfile) { this.profile = profile }
    override suspend fun updatePassword(newPassword: String) { /* no-op for fake */ }
}
