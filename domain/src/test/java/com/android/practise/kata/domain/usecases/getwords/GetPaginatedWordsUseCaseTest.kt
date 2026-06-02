package com.android.practise.kata.domain.usecases.getwords

import androidx.paging.PagingData
import com.android.practise.kata.domain.repository.DictionaryRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetPaginatedWordsUseCaseTest :
    BehaviorSpec({
        val repository: DictionaryRepository = mockk()
        val useCase = GetPaginatedWordsUseCase(repository)

        Given("GetPaginatedWordsUseCase") {
            val pagingData = PagingData.empty<com.android.practise.kata.domain.model.DictionaryEntry>()

            When("fetching paginated words") {
                coEvery { repository.getPaginatedWords() } returns flowOf(pagingData)
                val result = useCase().first()

                Then("it should return the paging flow from repository") {
                    result shouldNotBe null
                }
            }
        }
    })
