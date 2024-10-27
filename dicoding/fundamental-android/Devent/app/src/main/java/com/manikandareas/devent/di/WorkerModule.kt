package com.manikandareas.devent.di

import android.content.Context
import androidx.work.WorkManager
import org.koin.dsl.module

fun provideWorkManager(context: Context): WorkManager = WorkManager.getInstance(context)

val workManagerModule = module {
    single { provideWorkManager(get()) }
}