package com.android.practise.kata.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DictionaryEntryEntity::class], version = 1, exportSchema = false)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDao
}
