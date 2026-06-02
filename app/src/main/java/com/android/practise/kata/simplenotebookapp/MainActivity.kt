package com.android.practise.kata.simplenotebookapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.practise.kata.presentation.dictionary.AddDictionaryEntryScreen
import com.android.practise.kata.presentation.dictionary.WordListScreen
import com.android.practise.kata.presentation.dictionary.mvi.AddDictionaryEntryContract
import com.android.practise.kata.presentation.dictionary.mvi.WordListContract
import com.android.practise.kata.presentation.dictionary.mvi.viewmodel.AddDictionaryEntryViewModel
import com.android.practise.kata.presentation.dictionary.mvi.viewmodel.WordListViewModel
import com.android.practise.kata.simplenotebookapp.navigation.WordAdd
import com.android.practise.kata.simplenotebookapp.navigation.WordList
import com.android.practise.kata.simplenotebookapp.ui.theme.SimpleNoteBookAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleNoteBookAppTheme {
                val context = LocalContext.current
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = WordList,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable<WordList> {
                            val viewModel: WordListViewModel = hiltViewModel()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(Unit) {
                                viewModel.effect.collectLatest { effect ->
                                    when (effect) {
                                        WordListContract.WordListEffect.NavigateToAddWord -> {
                                            navController.navigate(WordAdd)
                                        }
                                    }
                                }
                            }

                            WordListScreen(
                                state = state,
                                dispatch = { event ->
                                    viewModel.event(event)
                                },
                            )
                        }

                        composable<WordAdd> {
                            val addDictionaryEntryViewModel: AddDictionaryEntryViewModel = hiltViewModel()
                            val state by addDictionaryEntryViewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(Unit) {
                                addDictionaryEntryViewModel.effect.collectLatest { effect ->
                                    when (effect) {
                                        AddDictionaryEntryContract.AddDictionaryEntryEffect.NavigateBack -> {
                                            Toast.makeText(context, "Entry saved!", Toast.LENGTH_SHORT).show()
                                            navController.popBackStack()
                                        }

                                        is AddDictionaryEntryContract.AddDictionaryEntryEffect.ShowToast -> {
                                            Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }

                            AddDictionaryEntryScreen(
                                state = state,
                                onIntent = { intent ->
                                    addDictionaryEntryViewModel.event(intent)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
