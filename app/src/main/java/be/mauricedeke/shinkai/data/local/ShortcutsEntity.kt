package be.mauricedeke.shinkai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shortcuts")
data class ShortcutsEntity(
    @PrimaryKey val id: Int = 0,
    val shortcuts: String = "EVENTS,KAART,LEXICON,TECHNIEKEN"
)
