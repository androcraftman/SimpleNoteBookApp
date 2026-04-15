package com.android.practise.kata.presentation.dictionary.viewmodel

import app.cash.turbine.test
import com.android.practise.kata.domain.usecases.addentry.SaveDictionaryEntryUseCase
import com.android.practise.kata.presentation.dictionary.mvi.AddDictionaryEntryContract
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddDictionaryEntryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val saveDictionaryEntryUseCase: SaveDictionaryEntryUseCase = mockk()
    private lateinit var viewModel: AddDictionaryEntryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AddDictionaryEntryViewModel(saveDictionaryEntryUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when OnWordChanged event is sent, state should be updated with new word`() = runTest {
        val word = "Kotlin"
        viewModel.event(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnWordChanged(word))

        viewModel.state.test {
            assertEquals(word, awaitItem().word)
        }
    }

    @Test
    fun `when OnMeaningChanged event is sent, state should be updated with new meaning`() = runTest {
        val meaning = "A language"
        viewModel.event(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnMeaningChanged(meaning))

        viewModel.state.test {
            assertEquals(meaning, awaitItem().meaning)
        }
    }

    @Test
    fun `when OnSaveClicked is sent and success, should emit NavigateBack effect`() = runTest {
        val word = "Kotlin"
        val meaning = "A language"
        coEvery { saveDictionaryEntryUseCase(word, meaning) } returns Result.success(Unit)

        viewModel.event(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnWordChanged(word))
        viewModel.event(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnMeaningChanged(meaning))

        viewModel.effect.test {
            viewModel.event(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnSaveClicked)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(AddDictionaryEntryContract.AddDictionaryEntryEffect.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `when OnSaveClicked is sent and failure, should update state with error`() = runTest {
        val word = "Kotlin"
        val meaning = "A language"
        val errorMessage = "Error saving"
        coEvery { saveDictionaryEntryUseCase(word, meaning) } returns Result.failure(Exception(errorMessage))

        viewModel.event(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnWordChanged(word))
        viewModel.event(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnMeaningChanged(meaning))
        viewModel.event(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnSaveClicked)

        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            assertEquals(errorMessage, awaitItem().error)
        }
    }
}
