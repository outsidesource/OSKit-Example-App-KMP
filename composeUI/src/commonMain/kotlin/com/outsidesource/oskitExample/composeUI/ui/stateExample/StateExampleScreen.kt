package com.outsidesource.oskitExample.composeUI.ui.stateExample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.composeUI.state.stateExample.StateExampleViewInteractor
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import com.outsidesource.oskitkmp.interactor.collectAsState
import com.outsidesource.oskitkmp.router.rememberForRoute
import org.koin.java.KoinJavaComponent.inject

@Composable
fun ViewStateExampleScreen(
    depth: Int,
    interactor: StateExampleViewInteractor = rememberForRoute { inject<StateExampleViewInteractor>(StateExampleViewInteractor::class.java).value }
) {
    val state by interactor.collectAsState()

    Screen("View State Example") {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Count: ${state.count}")
            Text("Computed Count: ${state.computedCount}")

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    content = { Text("Increment") },
                    onClick = interactor::increment
                )
                Button(
                    content = { Text("Decrement") },
                    onClick = interactor::decrement
                )
            }

            TextField(
                label = { Text("Validity Checker (only numbers)") },
                value = state.validityCheckerValue,
                isError = !state.isValidityCheckerValid,
                onValueChange = { interactor.validityCheckerValueChanged(it) }
            )

            Text("Screen Depth: $depth")
            Button(
                content = { Text("Push new screen") },
                onClick = { interactor.pushNewScreenButtonClicked(depth + 1) }
            )
            Button(
                content = { Text("Pop To Home") },
                onClick = { interactor.popToHome() }
            )
        }
    }
}