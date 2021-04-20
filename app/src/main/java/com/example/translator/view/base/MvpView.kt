package com.example.translator.view.base

import com.example.translator.model.data.AppState

interface MvpView {

    fun renderData(appState: AppState)
}
