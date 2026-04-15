package com.android.practise.kata.presentation.dictionary.mvi

import com.android.practise.kata.core_ui.mvi.MVIContract

interface AddDictionaryEntryContract :
    MVIContract<AddDictionaryEntryContract.AddDictionaryEntryState, AddDictionaryEntryContract.AddDictionaryEntryEffect, AddDictionaryEntryContract.AddDictionaryEntryEvent> {

    sealed class AddDictionaryEntryEvent {
        data class OnWordChanged(val word: String) : AddDictionaryEntryEvent()
        data class OnMeaningChanged(val meaning: String) : AddDictionaryEntryEvent()
        data object OnSaveClicked : AddDictionaryEntryEvent()
    }

    data class AddDictionaryEntryState(
        val word: String = "",
        val meaning: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed class AddDictionaryEntryEffect {
        data object NavigateBack : AddDictionaryEntryEffect()
        data class ShowToast(val message: String) : AddDictionaryEntryEffect()
    }
}
