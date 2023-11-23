package com.outsidesource.oskitExample.composeUI

import androidx.compose.ui.text.font.FontWeight
import com.outsidesource.oskitcompose.resources.KMPFont
import com.outsidesource.oskitcompose.resources.KMPFontFamily
import com.outsidesource.oskitcompose.resources.KMPImage
import com.outsidesource.oskitcompose.resources.resolveKmpFontFamily
import kotlinx.coroutines.runBlocking

object Images {
    val Penguin = KMPImage("images/penguin.png")
    val TuxXML = KMPImage("images/tux.xml")
    val TuxMonoXML = KMPImage("images/tux-mono.xml")
    val DarkMode = KMPImage("images/dark_mode.xml")
}

object Fonts {
    val SansSerifPro by lazy {
        val family = KMPFontFamily(
            listOf(
                KMPFont("fonts/SourceSansPro-Bold.ttf", weight = FontWeight.Bold),
                KMPFont("fonts/SourceSansPro-Light.ttf", weight = FontWeight.Light),
                KMPFont("fonts/SourceSansPro-Regular.ttf", weight = FontWeight.Normal),
                KMPFont("fonts/SourceSansPro-SemiBold.ttf", weight = FontWeight.SemiBold)
            )
        )
        runBlocking { resolveKmpFontFamily(family) }
    }
}