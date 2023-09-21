package com.outsidesource.oskitExample.composeUI

import com.outsidesource.oskitkmp.file.IKMPFileHandler
import com.outsidesource.oskitkmp.file.IOSKMPFileHandler
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { IOSKMPFileHandler() } bind IKMPFileHandler::class
}