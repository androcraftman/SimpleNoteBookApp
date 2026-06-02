package com.android.practise.kata.presentation.dictionary.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.android.practise.kata.domain.usecases.getwords.GetPaginatedWordsUseCase
import com.android.practise.kata.presentation.dictionary.mvi.WordListContract.WordListEffect
import com.android.practise.kata.presentation.dictionary.mvi.WordListContract.WordListEvent
import com.android.practise.kata.presentation.dictionary.mvi.viewmodel.WordListViewModel
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class WordListViewModelTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val testDispatcher = UnconfinedTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    val getPaginatedWordsUseCase = mockk<GetPaginatedWordsUseCase>()

    Given("WordListViewModel") {
        coEvery { getPaginatedWordsUseCase() } returns flowOf(PagingData.empty())

        When("OnAddWordClicked event is triggered") {
            Then("it should emit NavigateToAddWord effect") {
                val viewModel = WordListViewModel(getPaginatedWordsUseCase)

                viewModel.effect.test {
                    viewModel.event(WordListEvent.OnAddWordClicked)
                    awaitItem() shouldBe WordListEffect.NavigateToAddWord
                }
            }
        }
    }
})
