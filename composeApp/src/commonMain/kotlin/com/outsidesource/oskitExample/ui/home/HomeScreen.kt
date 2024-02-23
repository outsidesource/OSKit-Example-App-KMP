package com.outsidesource.oskitExample.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.resources.Strings
import com.outsidesource.oskitExample.ui.common.Screen
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.resources.rememberKmpString

@Composable
fun HomeScreen(
    interactor: HomeViewInteractor = rememberInjectForRoute(),
) {
    Screen("Home") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Button(
                content = { Text(rememberKmpString(Strings.appInteractorExample)) },
                onClick = interactor::appStateExampleButtonClicked,
            )
            Button(
                content = { Text(rememberKmpString(Strings.viewInteractorExample)) },
                onClick = interactor::viewStateExampleButtonClicked,
            )
            Button(
                content = { Text(rememberKmpString(Strings.fileHandling)) },
                onClick = interactor::fileHandlingButtonClicked,
            )
            Button(
                content = { Text(rememberKmpString(Strings.markdown)) },
                onClick = interactor::markdownButtonClicked,
            )
            Button(
                content = { Text(rememberKmpString(Strings.popups)) },
                onClick = interactor::popupsButtonClicked,
            )
            Button(
                content = { Text(rememberKmpString(Strings.resources)) },
                onClick = interactor::resourcesButtonClicked,
            )
            Button(
                content = { Text(rememberKmpString(Strings.iosServices)) },
                onClick = interactor::iosServicesButtonClicked,
            )
            Button(
                content = { Text("Widgets") },
                onClick = interactor::widgetsButtonClicked,
            )
        }
    }
}