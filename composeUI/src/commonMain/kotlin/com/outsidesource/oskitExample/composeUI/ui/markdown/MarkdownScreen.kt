package com.outsidesource.oskitExample.composeUI.ui.markdown

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.outsidesource.oskitExample.composeUI.Images
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import com.outsidesource.oskitcompose.markdown.Markdown
import com.outsidesource.oskitcompose.resources.rememberKmpImage

@Composable
fun MarkdownScreen() {
    val penguin = rememberKmpImage(Images.Penguin)
    val tux = rememberKmpImage(Images.TuxXML)

    Screen("Markdown") {
        Markdown(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            text = markdown,
            loadAsync = true,
            localImageMap = remember {
                mapOf(
                    "penguin" to penguin,
                    "tux" to tux,
                )
            }
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

    ![Minion](https://octodex.github.com/images/minion.png) 
    
    ![Stormtroopocat](https://octodex.github.com/images/stormtroopocat.jpg "The Stormtroopocat")

    ### Inline Images
    These ![](https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/Logo_CODE.svg/320px-Logo_CODE.svg.png) are inline images ![](https://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Kotlin_Icon.png/240px-Kotlin_Icon.png)

    ### Local Resource Images
    
    Align Center:
    
    ![](local:penguin,width:100,height:100,halign:center)
    
    Align End:
    
    ![](local:tux,width:100,height:100,halign:end)
""".trimIndent()