package com.android.practise.kata.data.local.datasource

import androidx.paging.PagingSource
import com.android.practise.kata.data.local.db.DictionaryDao
import com.android.practise.kata.data.local.db.DictionaryEntryEntity
import javax.inject.Inject

class DictionaryLocalDataSourceImpl
    @Inject
    constructor(
        private val dictionaryDao: DictionaryDao,
    ) : DictionaryLocalDataSource {
        override suspend fun saveEntry(
            word: String,
            meaning: String,
        ) {
            dictionaryDao.insertEntry(
                DictionaryEntryEntity(
                    word = word,
                    meaning = meaning,
                ),
            )
        }

        override fun getPaginatedEntries(): PagingSource<Int, DictionaryEntryEntity> {
            return dictionaryDao.getPaginatedEntries()
        }
    }
