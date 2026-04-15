package com.android.practise.kata.simplenotebookapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.android.practise.kata.presentation.dictionary.AddDictionaryEntryScreen
import com.android.practise.kata.presentation.dictionary.mvi.AddDictionaryEntryContract
import com.android.practise.kata.presentation.dictionary.viewmodel.AddDictionaryEntryViewModel
import com.android.practise.kata.simplenotebookapp.ui.theme.SimpleNoteBookAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val addDictionaryEntryViewModel: AddDictionaryEntryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleNoteBookAppTheme {
                val context = LocalContext.current
                val state by addDictionaryEntryViewModel.state.collectAsState()

                LaunchedEffect(Unit) {
                    addDictionaryEntryViewModel.effect.collectLatest { effect ->
                        when (effect) {
                            AddDictionaryEntryContract.AddDictionaryEntryEffect.NavigateBack -> {
                                Toast.makeText(context, "Entry saved!", Toast.LENGTH_SHORT).show()
                                 finish() // Usually handled by navigation, but here we can just toast for now or finish if it's the only screen
                            }

                            is AddDictionaryEntryContract.AddDictionaryEntryEffect.ShowToast -> {
                                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AddDictionaryEntryScreen(
                        state = state,
                        onIntent = { intent ->
                            addDictionaryEntryViewModel.event(intent)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddDictionaryEntryPreview() {
    SimpleNoteBookAppTheme {
        AddDictionaryEntryScreen(
            state = AddDictionaryEntryContract.AddDictionaryEntryState(
                word = "Example",
                meaning = "This is an example meaning."
            ),
            onIntent = {}
        )
    }
}