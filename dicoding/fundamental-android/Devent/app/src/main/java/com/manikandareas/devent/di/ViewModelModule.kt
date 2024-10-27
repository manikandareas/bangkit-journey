package com.manikandareas.devent.di

import androidx.work.WorkManager
import com.manikandareas.devent.domain.repositories.EventRepository
import com.manikandareas.devent.ui.activity.detail_event.DetailEventViewModel
import com.manikandareas.devent.ui.activity.main.MainViewModel
import com.manikandareas.devent.ui.activity.splash.SplashViewModel
import com.manikandareas.devent.ui.fragment.explore.ExploreViewModel
import com.manikandareas.devent.ui.fragment.favorite.FavoriteViewModel
import com.manikandareas.devent.ui.fragment.finished.FinishedViewModel
import com.manikandareas.devent.ui.fragment.preferences.PreferencesViewModel
import com.manikandareas.devent.ui.fragment.upcoming.UpcomingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


fun provideExploreViewModel(eventRepository: EventRepository): ExploreViewModel =
    ExploreViewModel(eventRepository)

fun provideFinishedViewModel(eventRepository: EventRepository): FinishedViewModel =
    FinishedViewModel(eventRepository)

fun provideUpcomingViewModel(eventRepository: EventRepository): UpcomingViewModel =
    UpcomingViewModel(eventRepository)

fun providePreferencesViewModel(
    eventRepository: EventRepository,
    workManager: WorkManager
): PreferencesViewModel = PreferencesViewModel(eventRepository, workManager)

fun provideFavoriteViewModel(eventRepository: EventRepository): FavoriteViewModel =
    FavoriteViewModel(eventRepository)

fun provideDetailEventViewModel(eventRepository: EventRepository): DetailEventViewModel =
    DetailEventViewModel(eventRepository)

fun provideMainViewModel(eventRepository: EventRepository): MainViewModel =
    MainViewModel(eventRepository)

fun provideSplashViewModel(eventRepository: EventRepository): SplashViewModel =
    SplashViewModel(eventRepository)


val viewModelModule = module {
    viewModel { provideExploreViewModel(get()) }
    viewModel { provideFinishedViewModel(get()) }
    viewModel { provideUpcomingViewModel(get()) }
    viewModel { providePreferencesViewModel(get(), get()) }
    viewModel { provideFavoriteViewModel(get()) }
    viewModel { provideDetailEventViewModel(get()) }
    viewModel { provideMainViewModel(get()) }
    viewModel { provideSplashViewModel(get()) }
}