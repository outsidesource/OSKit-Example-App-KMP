package com.outsidesource.oskitExample.common

import com.outsidesource.oskitExample.common.service.device.DeviceService
import com.outsidesource.oskitExample.common.state.device.DeviceInteractor
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}, extraModules: Array<Module> = emptyArray()) =
    startKoin {
        appDeclaration()
        modules(commonModule(), platformModule(), *extraModules)
    }

fun commonModule() = module {
    single { DeviceService() }

    single { DeviceInteractor(get()) }
}