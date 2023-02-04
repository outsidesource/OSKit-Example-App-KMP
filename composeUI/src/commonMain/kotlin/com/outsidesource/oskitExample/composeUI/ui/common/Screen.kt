package com.outsidesource.oskitExample.composeUI.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.composeUI.state.app.AppViewInteractor
import org.koin.java.KoinJavaComponent.inject

@Composable
fun Screen(
    title: String,
    interactor: AppViewInteractor = remember { inject<AppViewInteractor>(AppViewInteractor::class.java).value },
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        AppBar(title, interactor.hasBackStack(), interactor::appBarBackButtonPressed)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            content = content
        )
    }
}