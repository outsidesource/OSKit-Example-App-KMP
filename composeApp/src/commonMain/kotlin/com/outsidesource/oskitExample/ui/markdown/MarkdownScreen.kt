package com.outsidesource.oskitExample.ui.markdown

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.outsidesource.oskitExample.resources.Images
import com.outsidesource.oskitExample.ui.app.theme.AppTheme
import com.outsidesource.oskitExample.ui.common.Screen
import com.outsidesource.oskitcompose.markdown.Markdown
import com.outsidesource.oskitcompose.markdown.MarkdownStyles
import com.outsidesource.oskitcompose.resources.rememberKmpImagePainter
import com.outsidesource.oskitcompose.systemui.KMPWindowInsets
import com.outsidesource.oskitcompose.systemui.bottomInsets

@Composable
fun MarkdownScreen() {
    val penguin = rememberKmpImagePainter(Images.Penguin)
    val tux = rememberKmpImagePainter(Images.TuxXML)
    val defaultFontFamily = AppTheme.typography.defaultFontFamily
    val colors = AppTheme.colors

    val markdownStyles = remember(colors.fontColor) {
        MarkdownStyles()
            .withDefaultTextStyle(TextStyle(fontFamily = defaultFontFamily, color = colors.fontColor))
            .let { it.copy(
                codeTextStyle = it.codeTextStyle.merge(TextStyle(fontFamily = FontFamily.Monospace)),
                loaderBackgroundColor = colors.primary,
                linkTextStyle = it.linkTextStyle.merge(TextStyle(color = colors.primary))
            ) }
    }

    Screen("Markdown") {
        Markdown(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(KMPWindowInsets.bottomInsets),
            styles = markdownStyles,
            text = markdown,
            loadAsync = true,
            localImageMap = remember {
                mapOf(
                    "penguin" to penguin,
                    "tux" to tux,
                )
            },
        )
    }
}

val markdown = """
    Markdown Example
    ====================

    Setext 1
    ====================
    Setext 2
    --------------------

    # h1 Heading
    ## h2 Heading
    ### h3 Heading
    #### h4 Heading
    ##### h5 Heading
    ###### h6 Heading

    ## Horizontal Rules

    -------


    ## Emphasis

    **This is bold text**

    __This is bold text__

    *This is italic text*

    _This is italic text_


    ## Blockquotes

    > Blockquotes can also be nested...
    >> ...by using additional greater-than signs right next to each other...
    > > > ...or with spaces between arrows.


    ## Lists

    Unordered

    * Create an unordered list by starting a new line with `*`, `+`, or `-`
    * Sub-lists are made by indenting 2 spaces:
      * Marker character change forces new list start:
        * Ac tristique libero volutpat at
        * Facilisis in pretium nisl aliquet
        * Nulla volutpat aliquam velit
    * Very easy!

    Ordered

    1. Lorem ipsum dolor sit amet
    2. Consectetur adipiscing elit
    3. Integer molestie lorem at massa

    1. You can use sequential numbers...
    1. ...or keep all the numbers as `1.`

    Start numbering with offset:

    57. foo
    1. bar


    ## Code

    Inline `code`

    Indented code

        // Some comments
        if (condition) {
            println("Condition met!")
        }


    Block code "fences"

    ```
    Sample text here...
    ```

    ## Links

    Auto Link
    <https://github.com/outsidesource>

    Link with title
    [GitHub](https://github.com/outsidesource)


    ## Images

    This composable only supports PNG, JPG, BMP files as well as any other local files you can get Painter objects for.
    
    ### Block Images

    Align Center:
    
    ![attrs(width=200, height=200, hAlign=center) Minion](https://octodex.github.com/images/minion.png)
    
    Align End:
    
    ![attrs(width=200, height=200, hAlign=end) Stormtroopocat](https://octodex.github.com/images/stormtroopocat.jpg "The Stormtroopocat")

    ### Inline Images
    These ![attrs(height=30)](https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/Logo_CODE.svg/320px-Logo_CODE.svg.png) are inline images ![attrs(width=60)](https://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Kotlin_Icon.png/240px-Kotlin_Icon.png)

    ### Local Resource Images
    
    Align Start:
    
    ![attrs(width=100,height=100,hAlign=start)](local:penguin)
    
    Align End:
    
    ![attrs(width=100, height=100, hAlign=end) Description](local:tux)
""".trimIndent()