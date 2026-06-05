package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.data.local.room.dao.UserProfileDao
import be.mauricedeke.shinkai.data.local.room.entity.UserProfileEntity
import be.mauricedeke.shinkai.domain.model.UserProfile
import be.mauricedeke.shinkai.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao
) : UserRepository {

    override fun observeUserProfile(): Flow<UserProfile?> =
        userProfileDao.observe().map { entity ->
            entity?.let {
                UserProfile(
                    userId = it.userId,
                    name = it.name,
                    email = it.email,
                    belt = it.belt,
                    profilePictureUri = it.profilePictureUri
                )
            } ?: FakeDataSource.userProfile
        }

    override suspend fun getUserProfile(): UserProfile? {
        var entity = userProfileDao.get() ?: return FakeDataSource.userProfile

        if (entity.userId == null) {
            entity = entity.copy(userId = UUID.randomUUID())
            userProfileDao.upsert(entity)
        }

        return UserProfile(
            userId = entity.userId,
            name = entity.name,
            email = entity.email,
            belt = entity.belt,
            profilePictureUri = entity.profilePictureUri
        )
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        userProfileDao.upsert(
            UserProfileEntity(
                userId = profile.userId ?: UUID.randomUUID(),
                name = profile.name,
                email = profile.email,
                belt = profile.belt,
                profilePictureUri = profile.profilePictureUri
            )
        )
    }

    override suspend fun updatePassword(newPassword: String) { /* no-op for fake */ }
}
