package com.manikandareas.stories

import android.app.Application
import android.content.pm.ApplicationInfo
import coil.ImageLoader

import com.manikandareas.stories.di.dataSourceModule
import com.manikandareas.stories.di.navigationModule
import com.manikandareas.stories.di.networkModule
import com.manikandareas.stories.di.validationModule
import com.manikandareas.stories.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class StoriesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ImageLoader.Builder(this)
            .crossfade(true)
            .build()
        startKoin {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                androidLogger(Level.INFO)
            } else {
                androidLogger(Level.NONE)
            }
            androidContext(this@StoriesApplication)
            modules(
                listOf(
                    networkModule,
                    dataSourceModule,
                    validationModule,
                    navigationModule,
                    viewModelModule
                )
            )
        }
    }
}