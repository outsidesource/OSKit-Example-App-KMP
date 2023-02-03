package com.outsidesource.oskitExample.composeUI.state.app

import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitkmp.coordinator.Coordinator
import com.outsidesource.oskitkmp.interactor.Interactor
import org.koin.java.KoinJavaComponent.inject

class AppViewInteractor: Interactor<Unit>(initialState = Unit) {
    val coordinator: Coordinator by inject<AppCoordinator>(AppCoordinator::class.java)
}