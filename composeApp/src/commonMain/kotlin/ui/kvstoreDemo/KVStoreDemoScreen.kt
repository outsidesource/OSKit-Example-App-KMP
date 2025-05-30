package ui.kvstoreDemo

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import ui.capability.CapabilityScreenViewInteractor
import ui.capability.CapabilityType
import ui.common.Screen
import ui.common.Tab

@Composable
fun KVStoreDemoScreen(
    interactor: KVStoreDemoScreenViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    Screen("KV Store Demo") {

    }
}
