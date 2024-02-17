package com.outsidesource.oskitExample.common

import com.outsidesource.oskitExample.common.service.s3.IS3Service
import com.outsidesource.oskitExample.common.service.s3.NoOpS3Service
import com.outsidesource.oskitExample.common.service.swift.ISwiftExampleService
import com.outsidesource.oskitExample.common.service.swift.NoOpSwiftExampleService
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule(platformContext: PlatformContext) = module {
    single { NoOpS3Service() } bind IS3Service::class
    single { NoOpSwiftExampleService() } bind ISwiftExampleService::class
}

actual class PlatformContext