package ui.popups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ui.app.theme.AppTheme
import ui.common.Screen
import com.outsidesource.oskitcompose.geometry.PopupShape
import com.outsidesource.oskitcompose.geometry.PopupShapeCaretPosition
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.modifier.outerShadow
import com.outsidesource.oskitcompose.popup.*
import com.outsidesource.oskitcompose.systemui.KmpWindowInsets
import com.outsidesource.oskitcompose.systemui.bottom
import com.outsidesource.oskitcompose.systemui.vertical

@Composable
fun PopupsScreen(
    interactor: PopupsScreenViewInteractor = rememberInjectForRoute(),
) {
    val state = interactor.collectAsState()
    val popupShape = remember {
        PopupShape(
            cornerRadius = 4.dp,
            caretPosition = PopupShapeCaretPosition.Top,
        )
    }

    Screen("Popup") {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = interactor::modalButtonClicked) { Text("Modal") }
            Button(onClick = interactor::drawerButtonClicked) { Text("Drawer") }
            Button(onClick = interactor::bottomSheetButtonClicked) { Text("Bottom Sheet") }

            Box {
                Button(onClick = interactor::popoverButtonClicked) { Text("Popover") }
                Popover(
                    isVisible = state.isPopoverVisible,
                    onDismissRequest = interactor::popoverDismissed,
                    anchors = PopoverAnchors.ExternalBottomAlignCenter,
                    offset = DpOffset((-4).dp, (-4).dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 4.dp, end = 4.dp, start = 4.dp)
                            .outerShadow(4.dp, shape = popupShape)
                            .background(Color(0xFF333333), shape = popupShape)
                            .padding(8.dp)
                    ) {
                        Text("Popover", color = Color.White)
                    }
                }
            }
        }
    }

    Modal(
        isVisible = state.isModalVisible,
        onDismissRequest = interactor::modalDismissed,
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .height(200.dp)
        ) {
            Text(
                text = "Modal",
                color = AppTheme.colors.popupFontColor,
            )
        }
    }

    BottomSheet(
        isVisible = state.isBottomSheetVisible,
        onDismissRequest = interactor::bottomSheetDismissed,
    ) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(KmpWindowInsets.bottom)
                .height(200.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color(0xFFCCCCCC), CircleShape)
                    .height(4.dp)
                    .width(100.dp)
                    .bottomSheetSwipeToDismiss()
            )
            Text(
                text = "Bottom Sheet",
                color = AppTheme.colors.popupFontColor,
            )
        }
    }

    Drawer(
        isVisible = state.isDrawerVisible,
        onDismissRequest = interactor::drawerDismissed,
        styles = DrawerStyles(backgroundColor = Color(0xFFF2F2F2))
    ) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(KmpWindowInsets.vertical)
                .fillMaxSize()
        ) {
            Text(
                text = "Drawer",
                color = AppTheme.colors.popupFontColor,
            )
        }
    }
}