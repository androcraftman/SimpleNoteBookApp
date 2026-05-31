package com.android.practise.kata.presentation.dictionary

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.android.practise.kata.presentation.dictionary.mvi.AddDictionaryEntryContract
import org.junit.Rule
import org.junit.Test

class AddDictionaryEntryComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addDictionaryEntryScreen_elementsAreDisplayed() {
        // Given
        val state = AddDictionaryEntryContract.AddDictionaryEntryState()

        // When
        composeTestRule.setContent {
            AddDictionaryEntryScreen(
                state = state,
                onIntent = {}
            )
        }

        // Then
        composeTestRule.onNodeWithTag(AddDictionaryEntryTestTags.WORD_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(AddDictionaryEntryTestTags.MEANING_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(AddDictionaryEntryTestTags.SAVE_BUTTON).assertIsDisplayed()
    }

    @Test
    fun addDictionaryEntryScreen_inputTriggersIntent() {
        // Given
        val intents = mutableListOf<AddDictionaryEntryContract.AddDictionaryEntryEvent>()

        // When
        composeTestRule.setContent {
            var state by androidx.compose.runtime.mutableStateOf(AddDictionaryEntryContract.AddDictionaryEntryState())

            AddDictionaryEntryScreen(
                state = state,
                onIntent = { 
                    intents.add(it)
                    when (it) {
                        is AddDictionaryEntryContract.AddDictionaryEntryEvent.OnWordChanged -> state = state.copy(word = it.word)
                        is AddDictionaryEntryContract.AddDictionaryEntryEvent.OnMeaningChanged -> state = state.copy(meaning = it.meaning)
                        else -> {}
                    }
                }
            )
        }

        val testWord = "Kotlin"
        composeTestRule.onNodeWithTag(AddDictionaryEntryTestTags.WORD_FIELD).performTextInput(testWord)
        
        val testMeaning = "Language"
        composeTestRule.onNodeWithTag(AddDictionaryEntryTestTags.MEANING_FIELD).performTextInput(testMeaning)

        composeTestRule.onNodeWithTag(AddDictionaryEntryTestTags.SAVE_BUTTON).performClick()

        // Then
        // We verify that the expected intents are present in the list.
        assert(intents.any { it is AddDictionaryEntryContract.AddDictionaryEntryEvent.OnWordChanged && it.word.contains(testWord) })
        assert(intents.any { it is AddDictionaryEntryContract.AddDictionaryEntryEvent.OnMeaningChanged && it.meaning.contains(testMeaning) })
        assert(intents.contains(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnSaveClicked))
    }
}
