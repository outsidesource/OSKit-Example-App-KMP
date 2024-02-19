package com.outsidesource.oskitExample.composeUI.ui.iosServices

import com.outsidesource.oskitExample.common.service.s3.IS3Service
import com.outsidesource.oskitExample.common.service.swift.ISwiftExampleService
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.outcome.unwrapOrReturn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

data class IOSServicesScreenViewState(
    val s3BucketListText: String = "",
    val createFlowInSwiftText: String = "",
    val collectFlowInSwiftText: String = "",
    val suspendFunctionText: String = "",
)

class IOSServicesScreenViewInteractor(
    private val s3Service: IS3Service,
    private val swiftExampleService: ISwiftExampleService,
) : Interactor<IOSServicesScreenViewState>(
    initialState = IOSServicesScreenViewState()
) {

    fun listS3BucketItemsClicked() {
        interactorScope.launch {
            update { state -> state.copy(s3BucketListText = "Loading...") }

            val files = s3Service.listS3Files().unwrapOrReturn {
                update { state -> state.copy(s3BucketListText = "Error") }
                return@launch
            }

            update { state -> state.copy(s3BucketListText = files.joinToString("\n")) }
        }
    }

    fun createFlowInSwiftClicked() {
        interactorScope.launch {
            swiftExampleService.createFlowInSwift().collect {
                update { state -> state.copy(createFlowInSwiftText = it) }
            }
            update { state -> state.copy(createFlowInSwiftText = "Completed") }
        }
    }

    fun collectFlowInSwiftClicked() {
        interactorScope.launch {
            val flow = flow {
                emit("One")
                delay(1_000)
                emit("Two")
                delay(1_000)
                emit("Three")
                delay(1_000)
                emit("Done!")
                delay(1_000)
            }

            update { state -> state.copy(collectFlowInSwiftText = "Collecting in Swift...") }
            swiftExampleService.collectFlowInSwift(flow)
            update { state -> state.copy(collectFlowInSwiftText = "Completed") }
        }
    }

    fun suspendFunctionClicked() {
        interactorScope.launch {
            update { state -> state.copy(suspendFunctionText = "Started...") }
            val result = swiftExampleService.suspendFunction()
            update { state -> state.copy(suspendFunctionText = result.toString()) }
        }
    }
}