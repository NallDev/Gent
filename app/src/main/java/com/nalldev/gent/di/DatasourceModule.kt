package com.nalldev.gent.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nalldev.gent.data.datasources.local.EventBookmarkDao
import com.nalldev.gent.data.datasources.local.EventDao
import com.nalldev.gent.data.datasources.local.LocalDatasource
import com.nalldev.gent.data.datasources.preference.PreferenceDatasource
import com.nalldev.gent.data.datasources.remote.ApiService
import com.nalldev.gent.data.datasources.remote.RemoteDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "configuration")

fun provideRemoteDatasource(apiService: ApiService, context: Context, ioDispatcher: CoroutineDispatcher): RemoteDatasource = RemoteDatasource(apiService, context, ioDispatcher)

fun provideLocalDatasource(eventDao: EventDao, eventBookmarkDao: EventBookmarkDao, ioDispatcher: CoroutineDispatcher): LocalDatasource = LocalDatasource(eventDao, eventBookmarkDao, ioDispatcher)

fun provideDataStore(context: Context): DataStore<Preferences> = context.dataStore

fun providePreferenceDatastore(dataStore: DataStore<Preferences>, ioDispatcher: CoroutineDispatcher): PreferenceDatasource = PreferenceDatasource(dataStore, ioDispatcher)

val datasourceModule = module {
    single(named("IODispatcher")) {
        Dispatchers.IO
    }
    single { provideDataStore(get()) }

    factory { provideRemoteDatasource(get(), get(), get(named("IODispatcher"))) }
    factory { provideLocalDatasource(get(), get(), get(named("IODispatcher"))) }
    factory { providePreferenceDatastore(get(), get(named("IODispatcher"))) }
}