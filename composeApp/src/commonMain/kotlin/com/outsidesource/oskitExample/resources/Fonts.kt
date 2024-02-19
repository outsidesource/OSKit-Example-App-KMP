package com.outsidesource.oskitExample.resources

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.outsidesource.oskitcompose.resources.KMPFont
import com.outsidesource.oskitcompose.resources.KMPFontFamily
import com.outsidesource.oskitcompose.resources.resolveKmpFontFamily
import kotlinx.coroutines.runBlocking

object Fonts {
    val Default = FontFamily.Default
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