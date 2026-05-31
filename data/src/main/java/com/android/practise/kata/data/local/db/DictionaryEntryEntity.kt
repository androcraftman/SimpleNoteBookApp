package com.android.practise.kata.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary_entries")
data class DictionaryEntryEntity(
    @PrimaryKey
    val word: String,
    val meaning: String,
    val timestamp: Long = System.currentTimeMillis(),
)
