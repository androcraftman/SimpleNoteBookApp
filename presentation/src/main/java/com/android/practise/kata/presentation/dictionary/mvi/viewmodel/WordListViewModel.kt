package com.android.practise.kata.presentation.dictionary.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.android.practise.kata.domain.usecases.getwords.GetPaginatedWordsUseCase
import com.android.practise.kata.presentation.dictionary.mvi.WordListContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordListViewModel
    @Inject
    constructor(
        getPaginatedWordsUseCase: GetPaginatedWordsUseCase,
    ) : ViewModel(), WordListContract {
        private val mutableEffect = MutableSharedFlow<WordListContract.WordListEffect>()
        override val effect: SharedFlow<WordListContract.WordListEffect> = mutableEffect.asSharedFlow()

        private val wordsFlow = getPaginatedWordsUseCase().cachedIn(viewModelScope)

        private val mutableUIState = MutableStateFlow(WordListContract.WordListState(wordsFlow = wordsFlow))
        override val state: StateFlow<WordListContract.WordListState> = mutableUIState.asStateFlow()

        override fun event(event: WordListContract.WordListEvent) {
            when (event) {
                WordListContract.WordListEvent.OnAddWordClicked -> {
                    viewModelScope.launch {
                        mutableEffect.emit(WordListContract.WordListEffect.NavigateToAddWord)
                    }
                }

                is WordListContract.WordListEvent.OnWordClicked -> {
                    viewModelScope.launch {
                        mutableEffect.emit(WordListContract.WordListEffect.NavigateToWordUpdate(event.word))
                    }
                }
            }
        }
    }
