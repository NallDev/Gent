package com.nalldev.gent.di

import com.nalldev.gent.data.datasources.local.EventDao
import com.nalldev.gent.data.datasources.local.LocalDatasource
import com.nalldev.gent.data.datasources.remote.ApiService
import com.nalldev.gent.data.datasources.remote.RemoteDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun provideRemoteDatasource(apiService: ApiService, ioDispatcher: CoroutineDispatcher): RemoteDatasource = RemoteDatasource(apiService, ioDispatcher)
fun provideLocalDatasource(eventDao: EventDao, ioDispatcher: CoroutineDispatcher): LocalDatasource = LocalDatasource(eventDao, ioDispatcher)

val datasourceModule = module {
    single(named("IODispatcher")) {
        Dispatchers.IO
    }

    factory { provideRemoteDatasource(get(), get(named("IODispatcher"))) }
    factory { provideLocalDatasource(get(), get(named("IODispatcher"))) }
}