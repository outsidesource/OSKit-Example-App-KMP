package resources

import com.outsidesource.oskitcompose.resources.KmpStringKey
import com.outsidesource.oskitcompose.resources.KmpStringSet
import com.outsidesource.oskitcompose.resources.KmpStrings

object Strings : KmpStrings() {
    val appInteractorExample = kmpStringKey()
    val viewInteractorExample = kmpStringKey()
    val fileSystem = kmpStringKey()
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

    override val locales: Map<String, KmpStringSet> = mapOf(
        "en" to EnStrings,
        "es" to EsStrings,
    )
}

private object EnStrings : KmpStringSet() {
    override val strings: Map<KmpStringKey, String> = mapOf(
        Strings.language to "English",

        Strings.appInteractorExample to "App Interactor Example",
        Strings.viewInteractorExample to "View Interactor Example",
        Strings.fileSystem to "File System",
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

private object EsStrings : KmpStringSet() {
    override val strings: Map<KmpStringKey, String> = mapOf(
        Strings.language to "Spanish",
        Strings.currentTime to "La hora actual es %s. ¡Qué hay sobre eso!",
    )
}