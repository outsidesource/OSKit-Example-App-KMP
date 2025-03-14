package ui.iosServices

import com.outsidesource.oskitkmp.interactor.Interactor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import service.swift.ISwiftExampleService

data class IOSServicesScreenViewState(
    val createFlowInSwiftText: String = "",
    val collectFlowInSwiftText: String = "",
    val suspendFunctionText: String = "",
)

class IOSServicesScreenViewInteractor(
    private val swiftExampleService: ISwiftExampleService,
) : Interactor<IOSServicesScreenViewState>(
    initialState = IOSServicesScreenViewState()
) {

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