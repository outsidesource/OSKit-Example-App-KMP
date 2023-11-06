package com.outsidesource.oskitExample.composeUI.ui.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outsidesource.oskitExample.composeUI.Images
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import com.outsidesource.oskitcompose.resources.KMPImage
import com.outsidesource.oskitcompose.resources.rememberKmpImagePainter

@Composable
fun ImagesScreen() {
    Screen("Images") {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            ImageWithCaption(Images.Penguin, "PNG Image")
            ImageWithCaption(Images.TuxXML, "VectorDrawable Image")
        }
    }
}

@Composable
fun ImageWithCaption(resource: KMPImage, caption: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(modifier = Modifier.size(200.dp), painter = rememberKmpImagePainter(resource), contentDescription = "")
        Text(caption, fontSize = 12.sp)
    }
}