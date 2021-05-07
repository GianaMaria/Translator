package com.example.translator.di

import androidx.room.Room
import com.example.model.data.DataModel
import com.example.repository.datasource.RetrofitImplementation
import com.example.repository.datasource.RoomDataBaseImplementation
import com.example.repository.repository.Repository
import com.example.repository.repository.RepositoryImplementation
import com.example.repository.repository.RepositoryImplementationLocal
import com.example.repository.repository.RepositoryLocal
import com.example.repository.room.HistoryDataBase
import com.example.translator.view.history.HistoryInteractor
import com.example.translator.view.history.HistoryViewModel
import com.example.translator.view.main.MainInteractor
import com.example.translator.view.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val application = module {
    single { Room.databaseBuilder(get(), HistoryDataBase::class.java, "HistoryDB").build() }
    single { get<HistoryDataBase>().historyDao() }

    single<Repository<List<DataModel>>> { RepositoryImplementation(RetrofitImplementation()) }

    single<RepositoryLocal<List<DataModel>>> {
        RepositoryImplementationLocal(RoomDataBaseImplementation(get()))
    }
}

val mainScreen = module {
    single {
        MainInteractor(get(), get())
    }
    viewModel {
        MainViewModel(get())
    }
}

val historyScreen = module {
    factory { HistoryViewModel(get()) }
    factory { HistoryInteractor(get(), get()) }
}