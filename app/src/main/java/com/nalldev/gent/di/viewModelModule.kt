package com.nalldev.gent.di

import com.nalldev.gent.domain.repositories.HomeRepository
import com.nalldev.gent.ui.fragment.explore.ExploreViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun provideExploreViewModel(homeRepository: HomeRepository): ExploreViewModel = ExploreViewModel(homeRepository)

val viewModelModule = module {
    viewModel { provideExploreViewModel(get()) }
}