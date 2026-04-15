package com.android.practise.kata.simplenotebookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.practise.kata.presentation.dictionary.AddDictionaryEntryScreen
import com.android.practise.kata.presentation.dictionary.mvi.AddDictionaryEntryContract
import com.android.practise.kata.simplenotebookapp.ui.theme.SimpleNoteBookAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleNoteBookAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var state by remember { mutableStateOf(AddDictionaryEntryContract.AddDictionaryEntryState()) }

                    AddDictionaryEntryScreen(
                        state = state,
                        onIntent = { intent ->
                            when (intent) {
                                is AddDictionaryEntryContract.AddDictionaryEntryEvent.OnWordChanged -> {
                                    state = state.copy(word = intent.word)
                                }
                                is AddDictionaryEntryContract.AddDictionaryEntryEvent.OnMeaningChanged -> {
                                    state = state.copy(meaning = intent.meaning)
                                }
                                AddDictionaryEntryContract.AddDictionaryEntryEvent.OnSaveClicked -> {
                                    println("Saved: ${state.word} - ${state.meaning}")
                                    state = AddDictionaryEntryContract.AddDictionaryEntryState() // Reset
                                }
                            }
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