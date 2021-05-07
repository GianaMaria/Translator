package com.example.core.viewModel

interface Interactor<T> {

    suspend fun getData(word: String, fromRemoteSource: Boolean): T
}