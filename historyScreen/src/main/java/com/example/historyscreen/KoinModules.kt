package com.example.historyscreen

import com.example.historyscreen.view.HistoryFragment
import com.example.historyscreen.view.HistoryInteractor
import com.example.historyscreen.view.HistoryViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun injectDependencies() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(listOf(historyScreen))
}

val historyScreen = module {
    scope(named<HistoryFragment>()) {
        viewModel { HistoryViewModel(get()) }
        scoped { HistoryInteractor(get(), get()) }
    }
}
