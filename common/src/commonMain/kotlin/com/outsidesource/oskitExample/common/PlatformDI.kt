package com.outsidesource.oskitExample.common

import org.koin.core.module.Module

expect fun platformModule(platformContext: PlatformContext): Module
expect class PlatformContext