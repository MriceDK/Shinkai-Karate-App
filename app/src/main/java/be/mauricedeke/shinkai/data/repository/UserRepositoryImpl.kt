package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.room.dao.UserProfileDao
import be.mauricedeke.shinkai.data.local.room.entity.UserProfileEntity
import be.mauricedeke.shinkai.data.remote.client.AuthClient
import be.mauricedeke.shinkai.data.remote.client.UserClient
import be.mauricedeke.shinkai.data.remote.mapper.toDomain
import be.mauricedeke.shinkai.data.remote.mapper.toDto
import be.mauricedeke.shinkai.domain.model.UserProfile
import be.mauricedeke.shinkai.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val userClient: UserClient,
    private val authClient: AuthClient
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
            }
        }

    override suspend fun getUserProfile(): UserProfile? {
        val remote = userClient.getProfile().getOrNull()?.toDomain()
        if (remote != null) {
            userProfileDao.upsert(
                UserProfileEntity(
                    userId = remote.userId ?: UUID.randomUUID(),
                    name = remote.name,
                    email = remote.email,
                    belt = remote.belt,
                    profilePictureUri = remote.profilePictureUri
                )
            )
            return remote
        }
        val entity = userProfileDao.get() ?: return null
        return UserProfile(
            userId = entity.userId,
            name = entity.name,
            email = entity.email,
            belt = entity.belt,
            profilePictureUri = entity.profilePictureUri
        )
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        userClient.updateProfile(profile.toDto())
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

    override suspend fun updatePassword(newPassword: String) {
        // No-op: password change requires the current password supplied by the caller.
        // Use AuthClient.changePassword(currentPassword, newPassword) directly from the ViewModel.
    }
}
