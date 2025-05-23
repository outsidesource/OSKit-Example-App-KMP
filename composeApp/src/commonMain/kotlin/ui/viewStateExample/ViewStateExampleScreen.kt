package ui.viewStateExample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import org.koin.core.parameter.parametersOf
import ui.common.Screen

@Composable
fun ViewStateExampleScreen(
    depth: Int,
    interactor: ViewStateExampleViewInteractor = rememberInjectForRoute { parametersOf(depth) }
) {
    val state = interactor.collectAsState()

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
                    onClick = interactor::incrementClicked
                )
                Button(
                    content = { Text("Decrement") },
                    onClick = interactor::decrementClicked
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