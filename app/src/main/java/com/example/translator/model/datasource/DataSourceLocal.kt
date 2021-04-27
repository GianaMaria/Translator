package com.example.translator.model.datasource

import com.example.translator.model.data.DataModel

class DataSourceLocal(private val remoteProvider: RoomDataBaseImplementation = RoomDataBaseImplementation()) :
    DataSource<List<DataModel>> {

    override suspend fun getData(text: String): List<DataModel> = remoteProvider.getData(text)
}
