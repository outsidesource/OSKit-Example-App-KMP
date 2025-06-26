package ui.openForResultExample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import ui.common.Screen

@Composable
fun OpenForResultExampleScreen(
    interactor: OpenForResultExampleViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    Screen("Open Route For Result Example") {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "This example shows how to launch a route and await a result from it.\n" +
                        "Press the button to open the confirmation screen and receive " +
                        "the user’s selection as the result.\n",
                textAlign = TextAlign.Center
            )

            Button(
                content = { Text("Open Confirmation Route") },
                onClick = interactor::openConfirmationRouteClicked
            )

            Text("Returned result: ${state.result?.toString()?:"Cancelled"}")

        }
    }
}