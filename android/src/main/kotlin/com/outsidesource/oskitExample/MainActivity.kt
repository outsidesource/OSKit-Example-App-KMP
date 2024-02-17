package com.outsidesource.oskitExample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.outsidesource.oskitExample.composeUI.ui.app.App
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.file.KMPFileHandler
import com.outsidesource.oskitkmp.file.KMPFileHandlerContext
import org.koin.core.component.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val handler by koinInjector.inject<KMPFileHandler>()
        handler.init(KMPFileHandlerContext(application, this))

        setContent {
            App()
        }
    }
}