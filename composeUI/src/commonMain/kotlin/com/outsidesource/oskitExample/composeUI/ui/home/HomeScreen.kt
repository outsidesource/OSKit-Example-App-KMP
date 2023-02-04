package com.outsidesource.oskitExample.composeUI.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.composeUI.state.home.HomeViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import org.koin.java.KoinJavaComponent.inject

@Composable
fun HomeScreen(
    interactor: HomeViewInteractor = remember { inject<HomeViewInteractor>(HomeViewInteractor::class.java).value }
) {
    Screen("Home") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Button(
                content = { Text("Coordinator Example") },
                onClick = { interactor.coordinatorExampleButtonClicked() }
            )
            Button(
                content = { Text("View State Example") },
                onClick = { interactor.viewStateExampleButtonClicked() }
            )
        }
    }
}