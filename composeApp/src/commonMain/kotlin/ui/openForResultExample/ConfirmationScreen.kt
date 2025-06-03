package ui.openForResultExample

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
import ui.common.Screen

@Composable
fun ConfirmationScreen(
    interactor: ConfirmationViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    Screen("Example Route with Result") {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Did you just sign in from a new device?")

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    content = { Text("Yes, that was me") },
                    onClick = interactor::yesClicked
                )
                Button(
                    content = { Text("No, that's not me") },
                    onClick = interactor::noClicked
                )
            }
        }
    }
}