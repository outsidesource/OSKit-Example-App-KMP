package com.outsidesource.oskitExample.common

import com.outsidesource.oskitExample.common.interactor.app.AppInteractor
import com.outsidesource.oskitExample.common.service.device.DeviceService
import com.outsidesource.oskitExample.common.interactor.device.DeviceInteractor
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}, platformContext: PlatformContext, extraModules: Array<Module> = emptyArray()) =
    startKoin {
        appDeclaration()
        modules(commonModule(), platformModule(platformContext), *extraModules)
    }

fun commonModule() = module {
    single { DeviceService() }

    single { DeviceInteractor(get()) }
    single { AppInteractor() }
}