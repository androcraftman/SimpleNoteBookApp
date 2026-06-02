package com.android.practise.kata.data.local.datasource

import androidx.paging.PagingSource
import com.android.practise.kata.data.local.db.DictionaryEntryEntity

interface DictionaryLocalDataSource {
    suspend fun saveEntry(
        word: String,
        meaning: String,
    )

    fun getPaginatedEntries(): PagingSource<Int, DictionaryEntryEntity>
}
