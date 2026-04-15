package com.android.practise.kata.presentation.dictionary

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.android.practise.kata.presentation.dictionary.mvi.AddDictionaryEntryContract

object AddDictionaryEntryTestTags {
    const val WORD_FIELD = "word_field"
    const val MEANING_FIELD = "meaning_field"
    const val SAVE_BUTTON = "save_button"
    const val LOADING_INDICATOR = "loading_indicator"
    const val ERROR_TEXT = "error_text"
}

@Composable
fun AddDictionaryEntryScreen(
    state: AddDictionaryEntryContract.AddDictionaryEntryState,
    onIntent: (AddDictionaryEntryContract.AddDictionaryEntryEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.word,
                onValueChange = {
                    onIntent(
                        AddDictionaryEntryContract.AddDictionaryEntryEvent.OnWordChanged(
                            it
                        )
                    )
                },
                label = { Text("Word") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(AddDictionaryEntryTestTags.WORD_FIELD),
                enabled = !state.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.meaning,
                onValueChange = {
                    onIntent(
                        AddDictionaryEntryContract.AddDictionaryEntryEvent.OnMeaningChanged(
                            it
                        )
                    )
                },
                label = { Text("Meaning") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(AddDictionaryEntryTestTags.MEANING_FIELD),
                enabled = !state.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag(AddDictionaryEntryTestTags.ERROR_TEXT)
                )
            }

            Button(
                onClick = { onIntent(AddDictionaryEntryContract.AddDictionaryEntryEvent.OnSaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(AddDictionaryEntryTestTags.SAVE_BUTTON),
                enabled = !state.isLoading && state.word.isNotBlank() && state.meaning.isNotBlank()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .testTag(AddDictionaryEntryTestTags.LOADING_INDICATOR),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Save")
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AddDictionaryEntryPreview() {
    MaterialTheme {
        Surface {
            AddDictionaryEntryScreen(
                state = AddDictionaryEntryContract.AddDictionaryEntryState(
                    word = "Kotlin",
                    meaning = "A modern programming language that makes developers happier."
                ),
                onIntent = {}
            )
        }
    }
}
