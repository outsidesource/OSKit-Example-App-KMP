package com.outsidesource.oskitExample.common

import com.outsidesource.oskitExample.common.service.IOSS3Service
import com.outsidesource.oskitExample.common.service.s3.IS3Service
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule(platformContext: PlatformContext) = module {
    single { IOSS3Service() } bind IS3Service::class
}

actual class PlatformContext