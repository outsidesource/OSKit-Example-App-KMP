package com.outsidesource.oskitExample

import com.outsidesource.oskitkmp.storage.AndroidKMPStorage
import com.outsidesource.oskitkmp.storage.IKMPStorage
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { AndroidKMPStorage(appContext = get()) } bind IKMPStorage::class
}