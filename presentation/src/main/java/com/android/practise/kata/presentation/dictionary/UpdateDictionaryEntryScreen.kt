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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.android.practise.kata.presentation.dictionary.mvi.UpdateDictionaryEntryContract

object UpdateDictionaryEntryTestTags {
    const val WORD_FIELD = "update_word_field"
    const val MEANING_FIELD = "update_meaning_field"
    const val UPDATE_BUTTON = "update_button"
    const val LOADING_INDICATOR = "update_loading_indicator"
    const val ERROR_TEXT = "update_error_text"
}

@Composable
fun UpdateDictionaryEntryScreen(
    state: UpdateDictionaryEntryContract.UpdateDictionaryEntryState,
    onIntent: (UpdateDictionaryEntryContract.UpdateDictionaryEntryEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (state.isLoading && state.wordId.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.testTag(UpdateDictionaryEntryTestTags.LOADING_INDICATOR),
            )
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(16.dp),
            ) {
                OutlinedTextField(
                    value = state.word,
                    onValueChange = {
                        onIntent(
                            UpdateDictionaryEntryContract.UpdateDictionaryEntryEvent.OnWordChanged(
                                it,
                            ),
                        )
                    },
                    label = { Text("Word") },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .testTag(UpdateDictionaryEntryTestTags.WORD_FIELD),
                    enabled = !state.isLoading,
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.meaning,
                    onValueChange = {
                        onIntent(
                            UpdateDictionaryEntryContract.UpdateDictionaryEntryEvent.OnMeaningChanged(
                                it,
                            ),
                        )
                    },
                    label = { Text("Meaning") },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .testTag(UpdateDictionaryEntryTestTags.MEANING_FIELD),
                    enabled = !state.isLoading,
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (state.error != null) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .testTag(UpdateDictionaryEntryTestTags.ERROR_TEXT),
                    )
                }

                Button(
                    onClick = {
                        onIntent(
                            UpdateDictionaryEntryContract.UpdateDictionaryEntryEvent.OnUpdateClicked,
                        )
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .testTag(UpdateDictionaryEntryTestTags.UPDATE_BUTTON),
                    enabled = !state.isLoading && state.word.isNotBlank() && state.meaning.isNotBlank(),
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text("Update")
                    }
                }
            }
        }
    }
}

class UpdateDictionaryEntryStateProvider :
    PreviewParameterProvider<UpdateDictionaryEntryContract.UpdateDictionaryEntryState> {
    override val values: Sequence<UpdateDictionaryEntryContract.UpdateDictionaryEntryState> =
        sequenceOf(
            UpdateDictionaryEntryContract.UpdateDictionaryEntryState(
                wordId = "Kotlin",
                word = "Kotlin",
                meaning = "A modern programming language that makes developers happier.",
            ),
            UpdateDictionaryEntryContract.UpdateDictionaryEntryState(
                wordId = "Android",
                word = "Android",
                meaning = "An open-source operating system for mobile devices.",
                isLoading = true,
            ),
            UpdateDictionaryEntryContract.UpdateDictionaryEntryState(
                wordId = "",
                word = "",
                meaning = "",
                error = "Failed to load word details.",
            ),
        )
}

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun UpdateDictionaryEntryPreview(
    @PreviewParameter(UpdateDictionaryEntryStateProvider::class)
    state: UpdateDictionaryEntryContract.UpdateDictionaryEntryState,
) {
    MaterialTheme {
        Surface {
            UpdateDictionaryEntryScreen(
                state = state,
                onIntent = {},
            )
        }
    }
}
