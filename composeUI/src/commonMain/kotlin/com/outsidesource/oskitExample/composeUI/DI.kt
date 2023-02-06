package com.outsidesource.oskitExample.composeUI

import com.outsidesource.oskitExample.common.service.device.DeviceService
import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitExample.composeUI.state.app.AppViewInteractor
import com.outsidesource.oskitExample.composeUI.state.appStateExample.AppStateExampleViewInteractor
import com.outsidesource.oskitExample.composeUI.state.device.DeviceHomeViewInteractor
import com.outsidesource.oskitExample.composeUI.state.home.HomeViewInteractor
import com.outsidesource.oskitExample.composeUI.state.viewStateExample.ViewStateExampleViewInteractor
import org.koin.dsl.module

val composeUIModule = module {
    single { AppCoordinator() }

    factory { AppViewInteractor() }
    factory { HomeViewInteractor(get()) }
    factory { ViewStateExampleViewInteractor(get()) }
    factory { AppStateExampleViewInteractor(get(), get()) }
    factory { params -> DeviceHomeViewInteractor(params[0], get()) }

    single { DeviceService() }
} + platformModule()