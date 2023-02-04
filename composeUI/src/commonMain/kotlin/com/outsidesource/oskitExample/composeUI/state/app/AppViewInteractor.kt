package com.outsidesource.oskitExample.composeUI.state.app

import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitkmp.interactor.Interactor
import org.koin.java.KoinJavaComponent.inject

class AppViewInteractor: Interactor<Unit>(initialState = Unit) {
    val coordinator by inject<AppCoordinator>(AppCoordinator::class.java)

    fun hasBackStack() = coordinator.coordinatorHasBackStack()
    fun appBarBackButtonPressed() = coordinator.popBackStack()
}