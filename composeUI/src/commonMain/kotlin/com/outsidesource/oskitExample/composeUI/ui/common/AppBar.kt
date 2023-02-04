package com.outsidesource.oskitExample.composeUI.ui.common

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
fun AppBar(title: String, hasBackStack: Boolean, onBackPress: () -> Unit) {
    TopAppBar(
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