package com.android.practise.kata.presentation.dictionary.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.practise.kata.core.functional.stateInWhileActive
import com.android.practise.kata.domain.usecases.addentry.SaveDictionaryEntryUseCase
import com.android.practise.kata.presentation.dictionary.mvi.AddDictionaryEntryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.android.practise.kata.core.functional.Either
import com.android.practise.kata.core.error.getErrorMessage

@HiltViewModel
class AddDictionaryEntryViewModel @Inject constructor(
    private val saveDictionaryEntryUseCase: SaveDictionaryEntryUseCase
) : ViewModel(), AddDictionaryEntryContract {

    private val mutableUIState: MutableStateFlow<AddDictionaryEntryContract.AddDictionaryEntryState> =
        MutableStateFlow(AddDictionaryEntryContract.AddDictionaryEntryState())

    private val mutableEffect: MutableSharedFlow<AddDictionaryEntryContract.AddDictionaryEntryEffect> =
        MutableSharedFlow()

    override val state: StateFlow<AddDictionaryEntryContract.AddDictionaryEntryState> =
        mutableUIState.asStateFlow()

    override val effect: SharedFlow<AddDictionaryEntryContract.AddDictionaryEntryEffect>
        get() = mutableEffect.asSharedFlow()

    override fun event(event: AddDictionaryEntryContract.AddDictionaryEntryEvent) {
        when (event) {
            is AddDictionaryEntryContract.AddDictionaryEntryEvent.OnWordChanged -> {
                updateState { it.copy(word = event.word) }
            }

            is AddDictionaryEntryContract.AddDictionaryEntryEvent.OnMeaningChanged -> {
                updateState { it.copy(meaning = event.meaning) }
            }

            AddDictionaryEntryContract.AddDictionaryEntryEvent.OnSaveClicked -> {
                saveEntry()
            }
        }
    }

    private fun saveEntry() {
        val currentState = mutableUIState.value
        updateState { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            saveDictionaryEntryUseCase(currentState.word, currentState.meaning).collect { either ->
                updateState { it.copy(isLoading = false) }
                when (either) {
                    is Either.Right -> {
                        if (either.value) {
                            mutableEffect.emit(AddDictionaryEntryContract.AddDictionaryEntryEffect.NavigateBack)
                        } else {
                            updateState { it.copy(error = "Failed to save entry") }
                        }
                    }
                    is Either.Left -> {
                        updateState { it.copy(error = either.value.getErrorMessage()) }
                    }
                }
            }
        }
    }

    private fun updateState(update: (AddDictionaryEntryContract.AddDictionaryEntryState) -> AddDictionaryEntryContract.AddDictionaryEntryState) {
        mutableUIState.update(update)
    }
}
