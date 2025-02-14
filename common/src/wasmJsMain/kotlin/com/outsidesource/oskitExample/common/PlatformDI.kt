package com.outsidesource.oskitExample.common

import com.outsidesource.oskitExample.common.service.s3.IS3Service
import com.outsidesource.oskitExample.common.service.swift.ISwiftExampleService
import com.outsidesource.oskitExample.common.service.swift.NoOpSwiftExampleService
import com.outsidesource.oskitkmp.outcome.Outcome
import org.koin.dsl.module
import org.koin.dsl.bind

actual fun platformModule(platformContext: PlatformContext) = module {
    single {
        object : IS3Service {
            override suspend fun listS3Files(): Outcome<List<String>, Any> {
                return Outcome.Ok(listOf("Hello"))
            }
        }
    } bind IS3Service::class
    single { NoOpSwiftExampleService() } bind ISwiftExampleService::class
}

actual object PlatformContext