package com.android.practise.kata.domain.repository

import com.android.practise.kata.core.error.Failure
import com.android.practise.kata.core.functional.Either
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun saveEntry(
        word: String,
        meaning: String,
    ): Flow<Either<Failure, Boolean>>
}
