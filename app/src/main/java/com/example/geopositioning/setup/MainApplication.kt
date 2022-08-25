package com.example.geopositioning.setup

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            //inject Android context
            androidContext(this@MainApplication)
            // use modules
            modules(mainScreenInjectionModule)
        }

    }
}