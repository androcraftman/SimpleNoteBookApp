package com.android.practise.kata.presentation.dictionary.viewmodel

import app.cash.turbine.test
import com.android.practise.kata.domain.usecases.addentry.SaveDictionaryEntryUseCase
import com.android.practise.kata.presentation.dictionary.mvi.AddDictionaryEntryContract.AddDictionaryEntryEffect
import com.android.practise.kata.presentation.dictionary.mvi.AddDictionaryEntryContract.AddDictionaryEntryEvent
import com.android.practise.kata.presentation.dictionary.mvi.viewmodel.AddDictionaryEntryViewModel
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import com.android.practise.kata.core.functional.Either
import com.android.practise.kata.core.error.Failure
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class AddDictionaryEntryViewModelTest: BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val testDispatcher = UnconfinedTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    val saveDictionaryEntryUseCase = mockk<SaveDictionaryEntryUseCase>()

    Given("AddDictionaryEntryViewModel") {

        When("OnWordChanged event is triggered") {
            val word = "Kotlin"

            Then("it should update state with the new word") {
                val viewModel = AddDictionaryEntryViewModel(saveDictionaryEntryUseCase)
                viewModel.event(AddDictionaryEntryEvent.OnWordChanged(word))
                viewModel.state.test {
                    expectMostRecentItem().word shouldBe word
                }
            }
        }

        When("OnMeaningChanged event is triggered") {
            val meaning = "A modern programming language"

            Then("it should update state with the new meaning") {
                val viewModel = AddDictionaryEntryViewModel(saveDictionaryEntryUseCase)
                viewModel.event(AddDictionaryEntryEvent.OnMeaningChanged(meaning))
                viewModel.state.test {
                    expectMostRecentItem().meaning shouldBe meaning
                }
            }
        }

        When("OnSaveClicked event is triggered and save is successful") {
            val word = "Kotlin"
            val meaning = "Language"

            Then("it should emit NavigateBack effect") {
                val viewModel = AddDictionaryEntryViewModel(saveDictionaryEntryUseCase)
                coEvery { saveDictionaryEntryUseCase(word, meaning) } returns flowOf(Either.Right(true))
                viewModel.event(AddDictionaryEntryEvent.OnWordChanged(word))
                viewModel.event(AddDictionaryEntryEvent.OnMeaningChanged(meaning))

                viewModel.effect.test {
                    viewModel.event(AddDictionaryEntryEvent.OnSaveClicked)
                    awaitItem() shouldBe AddDictionaryEntryEffect.NavigateBack
                }
            }
        }

        When("OnSaveClicked event is triggered and save fails") {
            val errorMessage = "An unknown error occurred: Failed to save"

            Then("it should update state with the error message") {
                val viewModel = AddDictionaryEntryViewModel(saveDictionaryEntryUseCase)
                coEvery { saveDictionaryEntryUseCase(any(), any()) } returns flowOf(
                    Either.Left(Failure.UnknownError(Exception("Failed to save")))
                )
                viewModel.event(AddDictionaryEntryEvent.OnWordChanged("Word"))
                viewModel.event(AddDictionaryEntryEvent.OnMeaningChanged("Meaning"))
                viewModel.event(AddDictionaryEntryEvent.OnSaveClicked)
                viewModel.state.test {
                    expectMostRecentItem().error shouldBe errorMessage
                }
            }
        }
    }
})
