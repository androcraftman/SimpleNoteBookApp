package com.android.practise.kata.domain.repository

interface DictionaryRepository {
    suspend fun saveEntry(word: String, meaning: String): Result<Unit>
}
