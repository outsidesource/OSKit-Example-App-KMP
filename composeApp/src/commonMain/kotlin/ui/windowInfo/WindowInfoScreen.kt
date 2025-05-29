package ui.windowInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.systemui.KmpAppLifecycle
import com.outsidesource.oskitcompose.systemui.KmpAppLifecycleObserver
import com.outsidesource.oskitcompose.systemui.containerSizeDp
import com.outsidesource.oskitcompose.systemui.widthSizeClass
import ui.common.Screen

@Composable
fun WindowInfoScreen() {
    Screen("Window Info") {
        val windowInfo = LocalWindowInfo.current
        var lifecycle by remember(Unit) { mutableStateOf(KmpAppLifecycleObserver.lifecycle.value) }
        var previousLifecycle by remember(Unit) { mutableStateOf<KmpAppLifecycle?>(null) }

        LaunchedEffect(Unit) {
            KmpAppLifecycleObserver.lifecycle.collect {
                previousLifecycle = lifecycle
                lifecycle = it
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Size Class: ${windowInfo.widthSizeClass}")
            Text("Size: ${windowInfo.containerSizeDp}")
            Spacer(modifier = Modifier.height(24.dp))
            Text("Current Application Lifecycle: $lifecycle")
            Text("Previous Application Lifecycle: $previousLifecycle")
        }
    }
}