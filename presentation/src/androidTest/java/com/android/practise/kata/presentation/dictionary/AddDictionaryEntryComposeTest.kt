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
        val state = AddDictionaryEntryContract.AddDictionaryEntryState()

        // When
        composeTestRule.setContent {
            AddDictionaryEntryScreen(
                state = state,
                onIntent = { intents.add(it) }
            )
        }

        val testWord = "Kotlin"
        composeTestRule.onNodeWithTag(AddDictionaryEntryTestTags.WORD_FIELD).performTextInput(testWord)
        
        val testMeaning = "Language"
        composeTestRule.onNodeWithTag(AddDictionaryEntryTestTags.MEANING_FIELD).performTextInput(testMeaning)

        composeTestRule.onNodeWithTag(AddDictionaryEntryTestTags.SAVE_BUTTON).performClick()

        // Then
        // performTextInput triggers an intent for each character usually, or one if batching.
        // We verify that the expected intents are present in the list.
        assert(intents.any { it is AddDictionaryEntryContract.AddDictionaryEntryEvent.OnWordChanged && it.word.contains(testWord) })
        assert(intents.any { it is AddDictionaryEntryContract.AddDictionaryEntryEvent.OnMeaningChanged && it.meaning.contains(testMeaning) })
        assert(intents.contains(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnSaveClicked))
    }
}
