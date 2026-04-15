package com.android.practise.kata.domain.usecases.addentry

import com.android.practise.kata.domain.repository.DictionaryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SaveDictionaryEntryUseCaseTest {

    private val repository: DictionaryRepository = mockk()
    private lateinit var useCase: SaveDictionaryEntryUseCase

    @Before
    fun setup() {
        useCase = SaveDictionaryEntryUseCase(repository)
    }

    @Test
    fun `invoke should call repository save and return success`() = runTest {
        val word = "Kotlin"
        val meaning = "Language"
        coEvery { repository.saveEntry(word, meaning) } returns Result.success(Unit)

        val result = useCase(word, meaning)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke should return failure when repository returns failure`() = runTest {
        val word = "Kotlin"
        val meaning = "Language"
        val exception = Exception("Failed to save")
        coEvery { repository.saveEntry(word, meaning) } returns Result.failure(exception)

        val result = useCase(word, meaning)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
