package com.outsidesource.oskitExample.resources

import com.outsidesource.oskitcompose.resources.KMPStringKey
import com.outsidesource.oskitcompose.resources.KMPStringSet
import com.outsidesource.oskitcompose.resources.KMPStrings

object Strings : KMPStrings() {
    val appInteractorExample = kmpStringKey()
    val viewInteractorExample = kmpStringKey()
    val fileHandling = kmpStringKey()
    val markdown = kmpStringKey()
    val popups = kmpStringKey()
    val resources = kmpStringKey()
    val iosServices = kmpStringKey()

    val strings = kmpStringKey()
    val fonts = kmpStringKey()
    val images = kmpStringKey()
    val pngImage = kmpStringKey()
    val vectorDrawableImages = kmpStringKey()
    val externalFontTest = kmpStringKey()
    val standardFontTest = kmpStringKey()
    val currentTime = kmpStringKey()
    val language = kmpStringKey()

    override val locales: Map<String, KMPStringSet> = mapOf(
        "en" to EnStrings,
        "es" to EsStrings,
    )
}

private object EnStrings : KMPStringSet() {
    override val strings: Map<KMPStringKey, String> = mapOf(
        Strings.language to "English",

        Strings.appInteractorExample to "App Interactor Example",
        Strings.viewInteractorExample to "View Interactor Example",
        Strings.fileHandling to "File Handling",
        Strings.markdown to "Markdown",
        Strings.popups to "Popups",
        Strings.resources to "Resources",
        Strings.iosServices to "iOS Services",

        Strings.strings to "Strings",
        Strings.fonts to "Fonts",
        Strings.images to "Images",
        Strings.pngImage to "PNG Image",
        Strings.vectorDrawableImages to "Vector Drawable Image",
        Strings.externalFontTest to "This is an external font resource.",
        Strings.standardFontTest to "This is the default font.",
        Strings.currentTime to "The current time is %s. How about that!",
    )
}

private object EsStrings : KMPStringSet() {
    override val strings: Map<KMPStringKey, String> = mapOf(
        Strings.language to "Spanish",
        Strings.currentTime to "La hora actual es %s. ¡Qué hay sobre eso!",
    )
}