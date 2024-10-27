package com.manikandareas.devent

import android.app.Application
import android.content.pm.ApplicationInfo
import com.manikandareas.devent.di.databaseModule
import com.manikandareas.devent.di.datasourceModule
import com.manikandareas.devent.di.networkModule
import com.manikandareas.devent.di.repositoryModule
import com.manikandareas.devent.di.viewModelModule
import com.manikandareas.devent.di.workManagerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                androidLogger(Level.INFO)
            } else {
                androidLogger(Level.NONE)
            }
            androidContext(this@MainApplication)
            modules(
                listOf(
                    networkModule,
                    databaseModule,
                    datasourceModule,
                    repositoryModule,
                    viewModelModule,
                    workManagerModule
                )
            )
        }
    }
}