package com.android.practise.kata.data.local.datasource

interface DictionaryLocalDataSource {
    suspend fun saveEntry(
        word: String,
        meaning: String,
    )
}
