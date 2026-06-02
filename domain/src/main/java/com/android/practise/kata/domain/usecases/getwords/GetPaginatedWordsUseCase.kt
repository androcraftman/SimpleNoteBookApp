package com.android.practise.kata.domain.usecases.getwords

import androidx.paging.PagingData
import com.android.practise.kata.domain.model.DictionaryEntry
import com.android.practise.kata.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaginatedWordsUseCase
    @Inject
    constructor(
        private val repository: DictionaryRepository,
    ) {
        operator fun invoke(): Flow<PagingData<DictionaryEntry>> {
            return repository.getPaginatedWords()
        }
    }
