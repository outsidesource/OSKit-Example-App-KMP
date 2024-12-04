package com.outsidesource.oskitExample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import ui.app.App
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.file.KmpFileHandler
import com.outsidesource.oskitkmp.file.KmpFileHandlerContext
import org.koin.core.component.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val fileHandler by koinInjector.inject<KmpFileHandler>()
        fileHandler.init(KmpFileHandlerContext(application, this))

        setContent {
            App()
        }
    }
}