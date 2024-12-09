package com.outsidesource.oskitExample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitkmp.filesystem.IKmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import org.koin.core.component.inject
import ui.app.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val fileHandler by koinInjector.inject<IKmpFs>()
        fileHandler.init(KmpFsContext(application, this))

        setContent {
            App()
        }
    }
}