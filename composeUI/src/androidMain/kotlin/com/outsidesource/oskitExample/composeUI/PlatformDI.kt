package com.outsidesource.oskitExample.composeUI

import com.outsidesource.oskitkmp.file.AndroidKMPFileHandler
import com.outsidesource.oskitkmp.file.IKMPFileHandler
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { AndroidKMPFileHandler() } bind IKMPFileHandler::class
}