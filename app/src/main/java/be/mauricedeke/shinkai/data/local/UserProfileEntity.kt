package be.mauricedeke.shinkai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 0,
    val userId: UUID? = null,
    val name: String = "",
    val email: String = "",
    val belt: String = "Yellow belt",
    val profilePictureUri: String? = null
)
