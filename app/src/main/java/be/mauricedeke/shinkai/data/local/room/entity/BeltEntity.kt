package be.mauricedeke.shinkai.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "belts")
data class BeltEntity(
    @PrimaryKey val name: String,
    val beltColor: String,
    val programmeJson: String
)
