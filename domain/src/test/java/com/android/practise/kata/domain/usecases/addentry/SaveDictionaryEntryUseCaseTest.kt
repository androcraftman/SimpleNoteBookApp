package com.android.practise.kata.domain.usecases.addentry

import com.android.practise.kata.core.error.Failure
import com.android.practise.kata.core.functional.Either
import com.android.practise.kata.domain.repository.DictionaryRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class SaveDictionaryEntryUseCaseTest : BehaviorSpec({
    val repository: DictionaryRepository = mockk()
    val useCase = SaveDictionaryEntryUseCase(repository)

    Given("a dictionary entry to save") {
        val word = "Kotlin"
        val meaning = "A modern programming language"

        When("saving the entry is successful") {
            coEvery { repository.saveEntry(word, meaning) } returns flowOf(Either.Right(true))
            val result = useCase(word, meaning).first()

            Then("it should return a success result") {
                result shouldBe Either.Right(true)
            }
        }

        When("saving the entry fails") {
            val exception = Exception("Network error")
            coEvery { repository.saveEntry(word, meaning) } returns flowOf(Either.Left(Failure.UnknownError(exception)))
            val result = useCase(word, meaning).first()

            Then("it should return a failure result with the correct exception") {
                result shouldBe Either.Left(Failure.UnknownError(exception))
            }
        }
    }
})
