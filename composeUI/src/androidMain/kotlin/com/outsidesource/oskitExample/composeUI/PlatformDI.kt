package com.outsidesource.oskitExample.composeUI

import com.outsidesource.oskitkmp.storage.KMPStorage
import com.outsidesource.oskitkmp.storage.KMPStorageContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single { KMPStorage(context = KMPStorageContext(appName = "OSKit-Example-App-KMP", appContext = get())) }
}