package com.outsidesource.oskitExample.common.service.swift

import com.outsidesource.oskitkmp.outcome.Outcome
import kotlinx.coroutines.flow.Flow

interface ISwiftExampleService {
    fun createFlowInSwift(): Flow<String>
    suspend fun collectFlowInSwift(flow: Flow<String>)
    suspend fun suspendFunction(): Outcome<String, Any>
}

class NoOpSwiftExampleService : ISwiftExampleService {
    override fun createFlowInSwift(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun collectFlowInSwift(flow: Flow<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun suspendFunction(): Outcome<String, Any> {
        TODO("Not yet implemented")
    }
}