package com.outsidesource.oskitExample.composeUI

import com.outsidesource.oskitExample.common.service.device.DeviceService
import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitExample.composeUI.state.app.AppViewInteractor
import com.outsidesource.oskitExample.composeUI.state.home.HomeViewInteractor
import com.outsidesource.oskitExample.composeUI.state.stateExample.StateExampleViewInteractor
import org.koin.dsl.module

val composeUIModule = module {
    single { AppCoordinator() }

    single { AppViewInteractor() }
    factory { HomeViewInteractor(get()) }
    factory { StateExampleViewInteractor(get()) }

    single { DeviceService() }
} + platformModule()