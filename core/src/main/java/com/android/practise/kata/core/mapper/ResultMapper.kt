package com.android.practise.kata.core.mapper

fun interface ResultMapper<T, R> {
    fun map(input: T): R
}
