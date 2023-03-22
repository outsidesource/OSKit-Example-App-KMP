package com.outsidesource.oskitExample.composeUI.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.composeUI.state.home.HomeViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.popup.*
import org.koin.java.KoinJavaComponent.inject

@Composable
fun HomeScreen(
    interactor: HomeViewInteractor = rememberInjectForRoute()
) {
    Screen("Home") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Button(
                content = { Text("App Interactor Example") },
                onClick = { interactor.appStateExampleButtonClicked() }
            )
            Button(
                content = { Text("View Interactor Example") },
                onClick = { interactor.viewStateExampleButtonClicked() }
            )
        }
    }
}