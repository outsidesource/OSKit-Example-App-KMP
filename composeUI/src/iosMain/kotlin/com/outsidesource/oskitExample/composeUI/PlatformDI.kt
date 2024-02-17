package com.outsidesource.oskitExample.composeUI

import com.outsidesource.oskitExample.common.PlatformContext
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.common.service.IOSS3Service
import com.outsidesource.oskitExample.common.service.s3.IS3Service
import com.outsidesource.oskitExample.common.service.swift.ISwiftExampleService
import org.koin.dsl.bind
import org.koin.dsl.module

private val koin = initKoin(
    platformContext = PlatformContext(),
    extraModules = composeUIModule.toTypedArray()
).koin

actual fun platformModule() = module {
    single { IOSS3Service() } bind IS3Service::class
}

fun loadKoinSwiftModules(swiftExampleService: ISwiftExampleService) {
    koin.loadModules(
        listOf(
            module {
                single { swiftExampleService } bind ISwiftExampleService::class
            }
        )
    )
}