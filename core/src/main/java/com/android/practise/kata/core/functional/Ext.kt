package com.android.practise.kata.core.functional

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.stateInWhileActive(
    scope: CoroutineScope,
    initialValue: T,
    onActive: () -> Unit
): StateFlow<T> {
    onActive()
    return this.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialValue
    )
}
