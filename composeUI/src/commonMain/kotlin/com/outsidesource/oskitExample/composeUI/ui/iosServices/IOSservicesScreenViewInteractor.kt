package com.outsidesource.oskitExample.composeUI.ui.iosServices

import com.outsidesource.oskitExample.common.service.s3.IS3Service
import com.outsidesource.oskitExample.common.service.swift.ISwiftExampleService
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.outcome.unwrapOrElse
import kotlinx.coroutines.launch

data class IOSServicesScreenViewState(
    val s3BucketListText: String = ""
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

            val files = s3Service.listS3Files().unwrapOrElse {
                update { state -> state.copy(s3BucketListText = "Error") }
                return@launch
            }

            update { state -> state.copy(s3BucketListText = files.joinToString("\n")) }
        }
    }
}