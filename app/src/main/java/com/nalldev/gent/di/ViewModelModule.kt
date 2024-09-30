package com.nalldev.gent.di

import androidx.work.WorkManager
import com.nalldev.gent.domain.repositories.EventRepository
import com.nalldev.gent.ui.activity.detail.DetailViewModel
import com.nalldev.gent.ui.activity.splash.SplashViewModel
import com.nalldev.gent.ui.fragment.about.AboutViewModel
import com.nalldev.gent.ui.fragment.bookmark.BookmarkViewModel
import com.nalldev.gent.ui.fragment.explore.ExploreViewModel
import com.nalldev.gent.ui.fragment.finished.FinishedViewModel
import com.nalldev.gent.ui.fragment.upcoming.UpcomingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun provideExploreViewModel(eventRepository: EventRepository): ExploreViewModel = ExploreViewModel(eventRepository)

fun provideUpcomingViewModel(eventRepository: EventRepository): UpcomingViewModel = UpcomingViewModel(eventRepository)

fun provideBookmarkViewModel(eventRepository: EventRepository): BookmarkViewModel = BookmarkViewModel(eventRepository)

fun provideFinishedViewModel(eventRepository: EventRepository): FinishedViewModel = FinishedViewModel(eventRepository)

fun provideDetailViewModel(eventRepository: EventRepository): DetailViewModel = DetailViewModel(eventRepository)

fun provideSplashViewModel(eventRepository: EventRepository): SplashViewModel = SplashViewModel(eventRepository)

fun provideAboutViewModel(eventRepository: EventRepository, workManager: WorkManager): AboutViewModel = AboutViewModel(eventRepository, workManager)

val viewModelModule = module {
    viewModel { provideExploreViewModel(get()) }
    viewModel { provideUpcomingViewModel(get()) }
    viewModel { provideBookmarkViewModel(get()) }
    viewModel { provideFinishedViewModel(get()) }
    viewModel { provideDetailViewModel(get()) }
    viewModel { provideSplashViewModel(get()) }
    viewModel { provideAboutViewModel(get(), get()) }
}