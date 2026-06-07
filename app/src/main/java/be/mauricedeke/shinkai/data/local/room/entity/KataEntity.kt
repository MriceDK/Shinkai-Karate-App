package be.mauricedeke.shinkai.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "katas")
data class KataEntity(
    @PrimaryKey val id: String,
    val name: String,
    val belt: String,
    val beltColor: String,
    val description: String,
    val moves: List<String>
)
