package com.nalldev.gent.di

import com.nalldev.gent.data.datasources.local.LocalDatasource
import com.nalldev.gent.data.datasources.remote.RemoteDatasource
import com.nalldev.gent.data.repositories.HomeRepositoryImpl
import com.nalldev.gent.domain.repositories.HomeRepository
import org.koin.dsl.module

fun provideHomeRepository(remoteDatasource: RemoteDatasource, localDatasource: LocalDatasource): HomeRepository = HomeRepositoryImpl(remoteDatasource, localDatasource)

val repositoryModule = module {
    factory { provideHomeRepository(get(), get()) }
}