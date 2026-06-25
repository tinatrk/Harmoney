package com.example.harmoney.app

import android.app.Application
import com.example.harmoney.core.di.dataModule
import com.example.harmoney.core.di.repositoryModule
import com.example.harmoney.core.di.useCaseModule
import com.example.harmoney.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}
