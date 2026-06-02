package com.android.practise.kata.presentation.dictionary.mvi

import androidx.paging.PagingData
import com.android.practise.kata.core_ui.mvi.MVIContract
import com.android.practise.kata.domain.model.DictionaryEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface WordListContract :
    MVIContract<
        WordListContract.WordListState,
        WordListContract.WordListEffect,
        WordListContract.WordListEvent,
        > {
    sealed class WordListEvent {
        data object OnAddWordClicked : WordListEvent()
    }

    data class WordListState(
        val wordsFlow: Flow<PagingData<DictionaryEntry>> = flowOf(PagingData.empty()),
    )

    sealed class WordListEffect {
        data object NavigateToAddWord : WordListEffect()
    }
}
