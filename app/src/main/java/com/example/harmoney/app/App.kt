package com.example.harmoney.app

import android.app.Application
import com.example.harmoney.BuildConfig
import com.example.harmoney.core.di.coreModule
import com.example.harmoney.core.di.dataModule
import com.example.harmoney.core.di.repositoryModule
import com.example.harmoney.core.di.useCaseModule
import com.example.harmoney.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@App)
            modules(
                coreModule,
                dataModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}
