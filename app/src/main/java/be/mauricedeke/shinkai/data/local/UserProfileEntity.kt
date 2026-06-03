package be.mauricedeke.shinkai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val belt: String = "Yellow belt",
    val profilePictureUri: String? = null
)
