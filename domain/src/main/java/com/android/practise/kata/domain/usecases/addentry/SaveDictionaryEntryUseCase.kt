package com.android.practise.kata.domain.usecases.addentry

import com.android.practise.kata.core.error.Failure
import com.android.practise.kata.core.functional.Either
import com.android.practise.kata.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveDictionaryEntryUseCase
    @Inject
    constructor(
        private val repository: DictionaryRepository,
    ) {
        operator fun invoke(
            word: String,
            meaning: String,
        ): Flow<Either<Failure, Boolean>> {
            return repository.saveEntry(word, meaning)
        }
    }
