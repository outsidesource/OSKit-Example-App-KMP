package com.outsidesource.oskitExample

import android.app.Application
import PlatformContext
import initKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(platformContext = PlatformContext(this)).koin
    }
}