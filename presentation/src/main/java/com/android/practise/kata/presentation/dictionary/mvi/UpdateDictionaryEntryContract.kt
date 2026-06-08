package com.android.practise.kata.presentation.dictionary.mvi

import com.android.practise.kata.core_ui.mvi.MVIContract

interface UpdateDictionaryEntryContract :
    MVIContract<
        UpdateDictionaryEntryContract.UpdateDictionaryEntryState,
        UpdateDictionaryEntryContract.UpdateDictionaryEntryEffect,
        UpdateDictionaryEntryContract.UpdateDictionaryEntryEvent,
        > {
    sealed class UpdateDictionaryEntryEvent {
        data class OnWordChanged(val word: String) : UpdateDictionaryEntryEvent()

        data class OnMeaningChanged(val meaning: String) : UpdateDictionaryEntryEvent()

        data object OnUpdateClicked : UpdateDictionaryEntryEvent()
    }

    data class UpdateDictionaryEntryState(
        val wordId: String = "",
        val word: String = "",
        val meaning: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed class UpdateDictionaryEntryEffect {
        data object NavigateBack : UpdateDictionaryEntryEffect()

        data class ShowToast(val message: String) : UpdateDictionaryEntryEffect()
    }
}
