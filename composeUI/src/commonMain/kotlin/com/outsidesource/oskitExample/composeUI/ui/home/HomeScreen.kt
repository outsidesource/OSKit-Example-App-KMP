package com.outsidesource.oskitExample.composeUI.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.composeUI.Images
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.resources.rememberKmpImage

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
                onClick = interactor::appStateExampleButtonClicked,
            )
            Button(
                content = { Text("View Interactor Example") },
                onClick = interactor::viewStateExampleButtonClicked,
            )
            Button(
                content = { Text("File Handling") },
                onClick = interactor::fileHandlingButtonClicked,
            )
        }
    }
}