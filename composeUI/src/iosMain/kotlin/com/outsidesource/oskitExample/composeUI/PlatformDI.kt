package com.outsidesource.oskitExample.composeUI

import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.common.service.IOSS3Service
import com.outsidesource.oskitExample.common.service.s3.IS3Service
import com.outsidesource.oskitExample.common.service.swift.ISwiftExampleService
import com.outsidesource.oskitkmp.file.IKMPFileHandler
import com.outsidesource.oskitkmp.file.IOSKMPFileHandler
import org.koin.dsl.bind
import org.koin.dsl.module

private val koin = initKoin(extraModules = composeUIModule.toTypedArray()).koin

actual fun platformModule() = module {
    single { IOSKMPFileHandler() } bind IKMPFileHandler::class
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