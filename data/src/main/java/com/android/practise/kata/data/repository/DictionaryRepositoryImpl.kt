package com.android.practise.kata.data.repository

import com.android.practise.kata.domain.repository.DictionaryRepository
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor() : DictionaryRepository {
    override suspend fun saveEntry(word: String, meaning: String): Result<Unit> {
        // Placeholder implementation for saving the entry
        // In a real app, this would call a Room DAO or a Retrofit service
        return Result.success(Unit)
    }
}
