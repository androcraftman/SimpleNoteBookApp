package com.android.practise.kata.data.repository

import com.android.practise.kata.core.error.Failure
import com.android.practise.kata.core.functional.Either
import com.android.practise.kata.data.local.datasource.DictionaryLocalDataSource
import com.android.practise.kata.domain.repository.DictionaryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class DictionaryRepositoryImpl(
    private val localDataSource: DictionaryLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : DictionaryRepository {

    @Inject
    constructor(localDataSource: DictionaryLocalDataSource) : this(localDataSource, Dispatchers.IO)

    override fun saveEntry(word: String, meaning: String): Flow<Either<Failure, Boolean>> = flow {
        try {
            localDataSource.saveEntry(word, meaning)
            emit(Either.Right(true))
        } catch (ioException: IOException) {
            emit(Either.Left(Failure.NetworkError(ioException)))
        } catch (exception: Exception) {
            emit(Either.Left(Failure.UnknownError(exception)))
        }
    }.flowOn(ioDispatcher)
}
