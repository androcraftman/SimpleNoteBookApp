package com.android.practise.kata.data.di

import android.content.Context
import androidx.room.Room
import com.android.practise.kata.data.local.datasource.DictionaryLocalDataSource
import com.android.practise.kata.data.local.datasource.DictionaryLocalDataSourceImpl
import com.android.practise.kata.data.local.db.DictionaryDao
import com.android.practise.kata.data.local.db.DictionaryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DictionaryDatabase {
        return Room.databaseBuilder(
            context,
            DictionaryDatabase::class.java,
            "dictionary_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDictionaryDao(database: DictionaryDatabase): DictionaryDao {
        return database.dictionaryDao()
    }

    @Provides
    @Singleton
    fun provideDictionaryLocalDataSource(
        dictionaryDao: DictionaryDao
    ): DictionaryLocalDataSource {
        return DictionaryLocalDataSourceImpl(dictionaryDao)
    }
}
