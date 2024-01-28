package com.outsidesource.oskitExample.common.service.swift

import kotlinx.coroutines.flow.Flow

interface ISwiftExampleService {
    fun flowFromSwift(): Flow<String>
    fun flowToSwift(flow: Flow<String>)
    suspend fun swiftAsyncFunction(): String
    suspend fun kotlinSuspendFunction(): String
}