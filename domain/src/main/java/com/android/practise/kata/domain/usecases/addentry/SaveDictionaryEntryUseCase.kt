package com.android.practise.kata.domain.usecases.addentry

import com.android.practise.kata.domain.repository.DictionaryRepository
import javax.inject.Inject

class SaveDictionaryEntryUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {
    suspend operator fun invoke(word: String, meaning: String): Result<Unit> {
        return repository.saveEntry(word, meaning)
    }
}