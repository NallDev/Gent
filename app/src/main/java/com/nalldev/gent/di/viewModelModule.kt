package com.nalldev.gent.di

import com.nalldev.gent.domain.repositories.HomeRepository
import com.nalldev.gent.ui.fragment.bookmark.BookmarkViewModel
import com.nalldev.gent.ui.fragment.explore.ExploreViewModel
import com.nalldev.gent.ui.fragment.finished.FinishedViewModel
import com.nalldev.gent.ui.fragment.upcoming.UpcomingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun provideExploreViewModel(homeRepository: HomeRepository): ExploreViewModel = ExploreViewModel(homeRepository)

fun provideUpcomingViewModel(homeRepository: HomeRepository): UpcomingViewModel = UpcomingViewModel(homeRepository)

fun provideBookmarkViewModel(homeRepository: HomeRepository): BookmarkViewModel = BookmarkViewModel(homeRepository)

fun provideFinishedViewModel(homeRepository: HomeRepository): FinishedViewModel = FinishedViewModel(homeRepository)

val viewModelModule = module {
    viewModel { provideExploreViewModel(get()) }
    viewModel { provideUpcomingViewModel(get()) }
    viewModel { provideBookmarkViewModel(get()) }
    viewModel { provideFinishedViewModel(get()) }
}