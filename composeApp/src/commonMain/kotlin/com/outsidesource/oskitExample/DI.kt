package com.outsidesource.oskitExample

import com.outsidesource.oskitExample.coordinator.AppCoordinator
import com.outsidesource.oskitExample.ui.app.AppViewInteractor
import com.outsidesource.oskitExample.ui.appStateExample.AppStateExampleViewInteractor
import com.outsidesource.oskitExample.ui.common.ScreenViewInteractor
import com.outsidesource.oskitExample.ui.device.DeviceHomeViewInteractor
import com.outsidesource.oskitExample.ui.file.FileHandlingViewInteractor
import com.outsidesource.oskitExample.ui.home.HomeViewInteractor
import com.outsidesource.oskitExample.ui.iosServices.IOSServicesScreenViewInteractor
import com.outsidesource.oskitExample.ui.popups.PopupsScreenViewInteractor
import com.outsidesource.oskitExample.ui.viewStateExample.ViewStateExampleViewInteractor
import com.outsidesource.oskitkmp.file.IKMPFileHandler
import com.outsidesource.oskitkmp.file.KMPFileHandler
import org.koin.dsl.bind
import org.koin.dsl.module

val composeAppModule = module {
    single { AppCoordinator() }
    single { KMPFileHandler() } bind IKMPFileHandler::class

    factory { AppViewInteractor(get()) }
    factory { ScreenViewInteractor(get(), get()) }
    factory { HomeViewInteractor(get()) }
    factory { ViewStateExampleViewInteractor(get()) }
    factory { AppStateExampleViewInteractor(get(), get()) }
    factory { params -> DeviceHomeViewInteractor(params[0], get()) }
    factory { FileHandlingViewInteractor(get()) }
    factory { PopupsScreenViewInteractor() }
    factory { IOSServicesScreenViewInteractor(get(), get()) }
} + platformModule()