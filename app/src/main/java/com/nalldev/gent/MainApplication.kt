package com.nalldev.gent

import android.app.Application
import android.content.pm.ApplicationInfo
import com.nalldev.gent.di.databaseModule
import com.nalldev.gent.di.datasourceModule
import com.nalldev.gent.di.networkModule
import com.nalldev.gent.di.repositoryModule
import com.nalldev.gent.di.viewModelModule
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
            modules(listOf(
                networkModule,
                databaseModule,
                datasourceModule,
                repositoryModule,
                viewModelModule
            ))
        }
    }
}