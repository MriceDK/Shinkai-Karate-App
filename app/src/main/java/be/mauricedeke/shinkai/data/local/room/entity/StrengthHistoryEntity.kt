package be.mauricedeke.shinkai.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "strength_history")
data class StrengthHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val score: Int,
    val unit: String,
    val beltColor: String?,
    val timestamp: Long
)
