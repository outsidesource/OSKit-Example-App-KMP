@file:OptIn(ExperimentalOSKitAPI::class)

package com.outsidesource.oskitExample.composeUI.ui.iosServices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.composeUI.resources.Strings
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.resources.rememberKmpString
import com.outsidesource.oskitkmp.annotation.ExperimentalOSKitAPI
import com.outsidesource.oskitkmp.lib.Platform
import com.outsidesource.oskitkmp.lib.current

@OptIn(ExperimentalOSKitAPI::class)
@Composable
fun IOSServicesScreen(
    interactor: IOSServicesScreenViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    Screen(rememberKmpString(Strings.iosServices)) {
        if (Platform.current != Platform.IOS) {
            Text("View this screen in iOS to see different ways of implementing platform dependent code on iOS")
            return@Screen
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Section("Implemented with Kotlin via Cocoapods and Objective-C interop") {
                Button(
                    content = { Text("List AWS S3 Bucket Items") },
                    onClick = interactor::listS3BucketItemsClicked
                )
                Text(state.s3BucketListText)
            }

            Section("Implemented with Swift") {
                Column{
                    Button(
                        content = { Text("Create Flow in Swift") },
                        onClick = interactor::createFlowInSwiftClicked
                    )
                    Text(state.createFlowInSwiftText)
                }
                Column {
                    Button(
                        content = { Text("Collect Flow in Swift") },
                        onClick = interactor::collectFlowInSwiftClicked
                    )
                    Text(state.collectFlowInSwiftText)
                }
                Column {
                    Button(
                        content = { Text("Suspend Function from Swift") },
                        onClick = interactor::suspendFunctionClicked
                    )
                    Text(state.suspendFunctionText)
                }
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionHeader(title)
        content()
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(text = text, style = TextStyle(fontWeight = FontWeight.Bold))
}