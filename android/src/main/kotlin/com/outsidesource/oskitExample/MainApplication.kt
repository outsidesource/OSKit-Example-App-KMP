package com.outsidesource.oskitExample

import android.app.Application
import com.outsidesource.oskitExample.common.loadKoinSwiftModules
import com.outsidesource.oskitExample.composeUI.composeUIModule

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        loadKoinSwiftModules(extraModules = composeUIModule.toTypedArray()).koin
    }
}