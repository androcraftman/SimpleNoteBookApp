package com.android.practise.kata.presentation.dictionary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.android.practise.kata.domain.model.DictionaryEntry
import com.android.practise.kata.presentation.dictionary.mvi.WordListContract

object WordListTestTags {
    const val ADD_FAB = "add_fab"
    const val WORD_LIST = "word_list"
    const val LOADING_INDICATOR = "loading_indicator"
    const val ERROR_TEXT = "error_text"
    const val EMPTY_TEXT = "empty_text"
}

@Composable
fun WordListScreen(
    state: WordListContract.WordListState,
    dispatch: (WordListContract.WordListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyPagingItems = state.wordsFlow.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { dispatch(WordListContract.WordListEvent.OnAddWordClicked) },
                modifier = Modifier.testTag(WordListTestTags.ADD_FAB),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Word",
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            when {
                // Initial Load state
                lazyPagingItems.loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag(WordListTestTags.LOADING_INDICATOR),
                    )
                }
                // Initial Error state
                lazyPagingItems.loadState.refresh is LoadState.Error -> {
                    val error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
                    Text(
                        text = error.localizedMessage ?: "An error occurred",
                        color = MaterialTheme.colorScheme.error,
                        modifier =
                            Modifier
                                .padding(16.dp)
                                .testTag(WordListTestTags.ERROR_TEXT),
                    )
                }
                // Empty state
                lazyPagingItems.itemCount == 0 -> {
                    Text(
                        text = "No words added yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.testTag(WordListTestTags.EMPTY_TEXT),
                    )
                }
                // Populate List
                else -> {
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .testTag(WordListTestTags.WORD_LIST),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = lazyPagingItems.itemKey { it.word },
                            contentType = lazyPagingItems.itemContentType { "word_item" },
                        ) { index ->
                            val entry = lazyPagingItems[index]
                            if (entry != null) {
                                WordItem(entry = entry)
                            }
                        }

                        // Append (Pagination Loading)
                        val appendState = lazyPagingItems.loadState.append
                        if (appendState is LoadState.Loading) {
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        } else if (appendState is LoadState.Error) {
                            item {
                                Text(
                                    text = appendState.error.localizedMessage ?: "Error loading more items",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WordItem(
    entry: DictionaryEntry,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Text(
                text = entry.word,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = entry.meaning,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@androidx.compose.ui.tooling.preview.Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun WordListPreview() {
    MaterialTheme {
        androidx.compose.material3.Surface {
            WordListScreen(
                state =
                    WordListContract.WordListState(
                        wordsFlow =
                            kotlinx.coroutines.flow.flowOf(
                                PagingData.from(
                                    listOf(
                                        DictionaryEntry("Kotlin", "A modern programming language."),
                                        DictionaryEntry("Android", "An open-source operating system for mobile devices."),
                                    ),
                                ),
                            ),
                    ),
                dispatch = {},
            )
        }
    }
}
