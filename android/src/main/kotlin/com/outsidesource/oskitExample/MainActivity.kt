package com.outsidesource.oskitExample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import com.outsidesource.oskitExample.common.initKoin
import com.outsidesource.oskitExample.composeUI.ui.app.App
import composeUIModule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initKoin(extraModules = composeUIModule.toTypedArray()).koin

        setContent {
            MaterialTheme {
                App()
            }
        }
    }
}