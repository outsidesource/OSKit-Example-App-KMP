package com.outsidesource.oskitExample

import android.app.Application
import PlatformContext
import initKoin
import org.koin.core.Koin
import org.koin.core.KoinApplication

lateinit var koin: Koin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        koin = initKoin(platformContext = PlatformContext(this)).koin
    }
}