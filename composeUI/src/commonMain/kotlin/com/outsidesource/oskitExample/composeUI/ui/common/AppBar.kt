package com.outsidesource.oskitExample.composeUI.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.outsidesource.oskitcompose.systemui.KMPWindowInsets
import com.outsidesource.oskitcompose.systemui.verticalInsets

@Composable
fun AppBar(title: String, hasBackStack: Boolean, onBackPress: () -> Unit) {
    TopAppBar(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .windowInsetsPadding(KMPWindowInsets.verticalInsets),
        title = { Text(title) },
        navigationIcon = if (!hasBackStack) null else { {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            } }
        }
    )
}