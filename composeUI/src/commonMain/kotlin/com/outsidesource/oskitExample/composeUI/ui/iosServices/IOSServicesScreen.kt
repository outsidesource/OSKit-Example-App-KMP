@file:OptIn(ExperimentalOSKitAPI::class)

package com.outsidesource.oskitExample.composeUI.ui.iosServices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.outsidesource.oskitExample.composeUI.resources.Strings
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import com.outsidesource.oskitcompose.resources.rememberKmpString
import com.outsidesource.oskitkmp.annotation.ExperimentalOSKitAPI
import com.outsidesource.oskitkmp.lib.Platform
import com.outsidesource.oskitkmp.lib.current

@OptIn(ExperimentalOSKitAPI::class)
@Composable
fun IOSServicesScreen() {
    Screen(rememberKmpString(Strings.iosServices)) {
        if (Platform.current != Platform.IOS) {
            Text("View this screen in iOS to see different ways of implementing platform dependent code on iOS")
            return@Screen
        }

        SectionHeader("Implemented with Kotlin via Cocoapods and Objective-C interop")
        // mDNS (only works on simulator without entitlement), API call (not really necessary), some library, platform specific code

        SectionHeader("Implemented with Swift")
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                content = { Text("Observe Flow") },
                onClick = {}
            )
            Text("")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                content = { Text("Suspend Function") },
                onClick = {}
            )
            Text("")
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(text = text, style = TextStyle(fontWeight = FontWeight.Bold))
}