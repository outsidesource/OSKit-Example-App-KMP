package com.outsidesource.oskitExample.composeUI.ui.app

import com.outsidesource.oskitExample.composeUI.coordinator.AppCoordinator
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.interactor.Interactor
import org.koin.core.component.inject

class AppViewInteractor: Interactor<Unit>(initialState = Unit) {
    val coordinator by koinInjector.inject<AppCoordinator>()

    fun hasBackStack() = coordinator.coordinatorHasBackStack()
    fun appBarBackButtonPressed() = coordinator.popBackStack()
}