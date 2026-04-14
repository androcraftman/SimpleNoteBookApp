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
import com.android.practise.kata.simplenotebookapp.ui.theme.SimpleNoteBookAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleNoteBookAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var word by remember { mutableStateOf("") }
                    var meaning by remember { mutableStateOf("") }

                    AddDictionaryEntryScreen(
                        word = word,
                        onWordChange = { word = it },
                        meaning = meaning,
                        onMeaningChange = { meaning = it },
                        onAddClick = {
                            // Handle add word action
                            println("Added Word: $word, Meaning: $meaning")
                            // Reset fields after adding
                            word = ""
                            meaning = ""
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
            word = "Example",
            onWordChange = {},
            meaning = "This is an example meaning.",
            onMeaningChange = {},
            onAddClick = {}
        )
    }
}