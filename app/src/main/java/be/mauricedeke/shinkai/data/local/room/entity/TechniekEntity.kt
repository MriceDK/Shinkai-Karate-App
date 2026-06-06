package be.mauricedeke.shinkai.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "technieken")
data class TechniekEntity(
    @PrimaryKey val id: String,   // "$beltName:$name"
    val name: String,
    val beltName: String,
    val description: String,
    val programma: String
)
