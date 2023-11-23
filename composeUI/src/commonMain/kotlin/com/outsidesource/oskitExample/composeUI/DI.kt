package com.outsidesource.oskitExample.composeUI

import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitExample.composeUI.ui.app.AppViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.common.ScreenViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.appStateExample.AppStateExampleViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.device.DeviceHomeViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.file.FileHandlingViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.home.HomeViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.popups.PopupsScreenViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.viewStateExample.ViewStateExampleViewInteractor
import org.koin.dsl.module

val composeUIModule = module {
    single { AppCoordinator() }

    factory { AppViewInteractor(get()) }
    factory { ScreenViewInteractor(get(), get()) }
    factory { HomeViewInteractor(get()) }
    factory { ViewStateExampleViewInteractor(get()) }
    factory { AppStateExampleViewInteractor(get(), get()) }
    factory { params -> DeviceHomeViewInteractor(params[0], get()) }
    factory { FileHandlingViewInteractor(get()) }
    factory { PopupsScreenViewInteractor() }
} + platformModule()