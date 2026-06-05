package be.mauricedeke.shinkai.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "strength_results")
data class StrengthResultEntity(
    @PrimaryKey val type: String,
    val bestScore: Int = 0
)
