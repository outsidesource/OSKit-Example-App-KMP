package com.outsidesource.oskitExample

import android.app.Application
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.composeUI.composeUIModule

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(extraModules = composeUIModule.toTypedArray()).koin
    }
}