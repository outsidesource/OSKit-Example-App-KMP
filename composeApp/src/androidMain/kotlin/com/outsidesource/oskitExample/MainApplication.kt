package com.outsidesource.oskitExample

import android.app.Application
import com.outsidesource.oskitExample.common.PlatformContext
import com.outsidesource.oskitExample.common.initKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            platformContext = PlatformContext(this),
            extraModules = composeAppModule.toTypedArray()
        ).koin
    }
}