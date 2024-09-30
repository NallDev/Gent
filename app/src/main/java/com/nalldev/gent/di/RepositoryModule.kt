package com.nalldev.gent.di

import android.content.Context
import com.nalldev.gent.data.datasources.local.LocalDatasource
import com.nalldev.gent.data.datasources.preference.PreferenceDatasource
import com.nalldev.gent.data.datasources.remote.RemoteDatasource
import com.nalldev.gent.data.repositories.EventRepositoryImpl
import com.nalldev.gent.domain.repositories.EventRepository
import org.koin.dsl.module

fun provideHomeRepository(remoteDatasource: RemoteDatasource, localDatasource: LocalDatasource, preferenceDatasource: PreferenceDatasource, context: Context): EventRepository = EventRepositoryImpl(remoteDatasource, localDatasource, preferenceDatasource, context)

val repositoryModule = module {
    factory { provideHomeRepository(get(), get(), get(), get()) }
}