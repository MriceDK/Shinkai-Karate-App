package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.UserProfileDao
import be.mauricedeke.shinkai.data.local.UserProfileEntity
import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.model.UserProfile
import be.mauricedeke.shinkai.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao
) : UserRepository {

    override suspend fun getUserProfile(): UserProfile? {
        val entity = userProfileDao.get()
        return if (entity != null) {
            UserProfile(
                name = entity.name,
                email = entity.email,
                belt = entity.belt,
                profilePictureUri = entity.profilePictureUri
            )
        } else {
            FakeDataSource.userProfile
        }
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        userProfileDao.upsert(
            UserProfileEntity(
                name = profile.name,
                email = profile.email,
                belt = profile.belt,
                profilePictureUri = profile.profilePictureUri
            )
        )
    }

    override suspend fun updatePassword(newPassword: String) { /* no-op for fake */ }
}
