package com.outsidesource.oskitExample.ui.iosServices

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.MapKit.MKMapView
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun UIKitViewExample(
    modifier: Modifier,
) = UIKitView(
    modifier = modifier,
    factory = {
        MKMapView()
    }
)