package com.android.practise.kata.presentation.dictionary

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import com.android.practise.kata.presentation.dictionary.mvi.WordListContract
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class WordListComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun wordListScreen_fabIsDisplayed() {
        // Given
        val state =
            WordListContract.WordListState(
                wordsFlow = flowOf(PagingData.empty()),
            )

        // When
        composeTestRule.setContent {
            WordListScreen(
                state = state,
                dispatch = {},
            )
        }

        // Then
        composeTestRule.onNodeWithTag(WordListTestTags.ADD_FAB).assertIsDisplayed()
    }

    @Test
    fun wordListScreen_fabClickTriggersEvent() {
        // Given
        val state =
            WordListContract.WordListState(
                wordsFlow = flowOf(PagingData.empty()),
            )
        var eventTriggered: WordListContract.WordListEvent? = null

        // When
        composeTestRule.setContent {
            WordListScreen(
                state = state,
                dispatch = { eventTriggered = it },
            )
        }

        composeTestRule.onNodeWithTag(WordListTestTags.ADD_FAB).performClick()

        // Then
        assert(eventTriggered == WordListContract.WordListEvent.OnAddWordClicked)
    }
}
