package com.android.practise.kata.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.paging.PagingSource

@Dao
interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: DictionaryEntryEntity)

    @Query("SELECT * FROM dictionary_entries ORDER BY timestamp DESC")
    suspend fun getAllEntries(): List<DictionaryEntryEntity>

    @Query("SELECT * FROM dictionary_entries ORDER BY word ASC")
    fun getPaginatedEntries(): PagingSource<Int, DictionaryEntryEntity>
}
