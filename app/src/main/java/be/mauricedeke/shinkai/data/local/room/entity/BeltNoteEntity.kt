package be.mauricedeke.shinkai.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "belt_notes")
data class BeltNoteEntity(
    @PrimaryKey val beltName: String,
    val note: String
)
