package be.mauricedeke.shinkai.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lexicon_entries")
data class LexiconEntryEntity(
    @PrimaryKey val id: String,
    val japaneseWord: String,
    val translation: String,
    val description: String
)
