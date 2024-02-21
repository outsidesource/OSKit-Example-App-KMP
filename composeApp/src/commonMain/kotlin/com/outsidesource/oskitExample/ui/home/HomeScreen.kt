package com.outsidesource.oskitExample.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.ui.common.Screen
import com.outsidesource.oskitcompose.lib.VarRef
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.modifier.kmpMouseScrollFilter
import com.outsidesource.oskitcompose.pointer.awaitFirstUp
import com.outsidesource.oskitkmp.concurrency.Debouncer
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    interactor: HomeViewInteractor = rememberInjectForRoute(),
) {
    Screen("Home") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
//            Button(
//                content = { Text(rememberKmpString(Strings.appInteractorExample)) },
//                onClick = interactor::appStateExampleButtonClicked,
//            )
//            Button(
//                content = { Text(rememberKmpString(Strings.viewInteractorExample)) },
//                onClick = interactor::viewStateExampleButtonClicked,
//            )
//            Button(
//                content = { Text(rememberKmpString(Strings.fileHandling)) },
//                onClick = interactor::fileHandlingButtonClicked,
//            )
//            Button(
//                content = { Text(rememberKmpString(Strings.markdown)) },
//                onClick = interactor::markdownButtonClicked,
//            )
//            Button(
//                content = { Text(rememberKmpString(Strings.popups)) },
//                onClick = interactor::popupsButtonClicked,
//            )
//            Button(
//                content = { Text(rememberKmpString(Strings.resources)) },
//                onClick = interactor::resourcesButtonClicked,
//            )
//            Button(
//                content = { Text(rememberKmpString(Strings.iosServices)) },
//                onClick = interactor::iosServicesButtonClicked,
//            )

            val wheelPickerState = rememberKmpWheelPickerState(isInfinite = true, initiallySelectedItemIndex = 0)
            var index by remember { mutableStateOf(0) }

            Button(onClick = { index = 5}) {
                Text("Hello")
            }

            KMPWheelPicker(
                modifier = Modifier
                    .width(100.dp)
                    .height(175.dp),
                items = (0..105).toList(),
                selectedIndex = index,
                key = { it },
                state = wheelPickerState,
                onChange = {
                    println("On Change: $it")
                    index = it
                }
            ) {
                Text(
                    modifier = Modifier
                        .height(25.dp)
                        .fillMaxWidth(),
                    text = it.toString(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


@Stable
data class KMPWheelPickerState(
    val initiallySelectedItemIndex: Int = 0,
    val isInfinite: Boolean = true,
): ScrollableState {
    val lazyListState: LazyListState = LazyListState(
        firstVisibleItemIndex = if (isInfinite) {
            INFINITE_OFFSET + initiallySelectedItemIndex
        } else {
            initiallySelectedItemIndex
        }
    )

    val itemHeight by derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.size?.toFloat() ?: 0f }
    val viewportHeight by derivedStateOf { lazyListState.layoutInfo.viewportSize.height.toFloat() }

    val selectedItemRawIndex by derivedStateOf {
        val layoutInfo = lazyListState.layoutInfo
        layoutInfo.visibleItemsInfo.find {
            it.offset + it.size - layoutInfo.viewportStartOffset > layoutInfo.viewportSize.height / 2
        }?.index ?: initiallySelectedItemIndex
    }

    val selectedItemBasedOnTopRawIndex by derivedStateOf {
        val layoutInfo = lazyListState.layoutInfo
        layoutInfo.visibleItemsInfo.firstOrNull {
            it.offset + it.size - layoutInfo.viewportStartOffset > (layoutInfo.viewportSize.height / 2) - (it.size / 2)
        }?.index ?: initiallySelectedItemIndex
    }

    internal val verticalPadding by derivedStateOf {
        val viewportHeight = lazyListState.layoutInfo.viewportSize.height.toFloat()
        val itemHeight = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.size?.toFloat() ?: 0f
        (((viewportHeight) - itemHeight) / 2).coerceAtLeast(0f)
    }

    override val isScrollInProgress: Boolean
        get() = lazyListState.isScrollInProgress

    override val canScrollBackward: Boolean
        get() = lazyListState.canScrollBackward

    override val canScrollForward: Boolean
        get() = lazyListState.canScrollForward

    override suspend fun scroll(scrollPriority: MutatePriority, block: suspend ScrollScope.() -> Unit) =
        lazyListState.scroll(scrollPriority, block)

    override fun dispatchRawDelta(delta: Float): Float = lazyListState.dispatchRawDelta(delta)

    suspend fun scrollToItem(index: Int) {
        lazyListState.scrollToItem(if (isInfinite) index + INFINITE_OFFSET else index)
    }

    suspend fun animateScrollToItem(index: Int) {
        lazyListState.animateScrollToItem(if (isInfinite) index + INFINITE_OFFSET else index)
    }

    companion object {
        fun Saver(): Saver<KMPWheelPickerState, *> = Saver(
            save = { listOf(it.isInfinite, it.selectedItemRawIndex) },
            restore = {
                KMPWheelPickerState(
                    isInfinite = it[0] as Boolean,
                    initiallySelectedItemIndex = it[1] as Int
                )
            }
        )
    }
}

@Composable
fun rememberKmpWheelPickerState(
    isInfinite: Boolean = true,
    initiallySelectedItemIndex: Int = 0,
) = rememberSaveable(initiallySelectedItemIndex, isInfinite, saver = KMPWheelPickerState.Saver()) {
    KMPWheelPickerState(
        initiallySelectedItemIndex = initiallySelectedItemIndex,
        isInfinite = isInfinite
    )
}

// TODO: Both pointerInputs and mouse scroll should only happen on desktop, but need to make sure that scroll cancellation and letting go still adjusts
// TODO: Add indicators
// TODO: Test changing values but not letting the value change in an interactor and make sure the wheel scrolls back

/**
 * [KMPWheelPicker] is a cross-platform wheel picker.
 *
 * Note: All items must be the same height.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun <T : Any> KMPWheelPicker(
    items: List<T>,
    selectedIndex: Int,
    key: (T) -> Any,
    state: KMPWheelPickerState,
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    onChange: (T) -> Unit,
    scrollEffect: KMPWheelPickerScrollEffect = KMPWheelPickerDefaults.ScrollEffectWheel,
    content: @Composable LazyItemScope.(T) -> Unit,
) {
    val velocityTracker = remember { VelocityTracker() }
    val scope = rememberCoroutineScope()
    val flingBehavior = rememberSnapFlingBehavior(state.lazyListState)
    val lastOnChangeValue = remember { VarRef(items[selectedIndex]) }
    val scrollDebouncer = remember { Debouncer(timeoutMillis = 250, scope = scope) }

    val paddingValues = with(LocalDensity.current) {
        remember(state.verticalPadding) {
            PaddingValues(vertical = state.verticalPadding.toDp())
        }
    }

    val handleOnChange = remember(items, state) {
        handleOnChange@ { index: Int ->
            val newValue = items[getItemsIndex(index, state, items.size)]
            if (lastOnChangeValue.value == newValue) return@handleOnChange
            lastOnChangeValue.value = newValue
            onChange(newValue)
        }
    }

    // Handle value changing outside WheelPicker
    LaunchedEffect(selectedIndex) {
        if (state.isScrollInProgress || lastOnChangeValue.value == items[selectedIndex]) return@LaunchedEffect
        lastOnChangeValue.value = items[selectedIndex]
        state.animateScrollToItem(selectedIndex)
    }

    LazyColumn(
        modifier = modifier
            .kmpMouseScrollFilter { _, _ ->
                scrollDebouncer.emit {
                    val index = if (state.isInfinite) state.selectedItemRawIndex - INFINITE_OFFSET else state.selectedItemRawIndex
                    state.animateScrollToItem(index)
                    handleOnChange(state.selectedItemRawIndex)
                }
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown()
                    scope.launch {
                        state.lazyListState.scrollBy(0f)
                    }

                    awaitFirstUp()
                    scope.launch {
                        if (state.lazyListState.isScrollInProgress) return@launch // Is flinging
                        state.lazyListState.scroll {
                            with(flingBehavior) {
                                performFling(0f)
                                handleOnChange(state.selectedItemRawIndex)
                            }
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        velocityTracker.resetTracking()
                    },
                    onDrag = { change, delta ->
                        velocityTracker.addPointerInputChange(change)
                        state.lazyListState.dispatchRawDelta(-delta.y)
                    },
                    onDragEnd = {
                        val velocity = -velocityTracker.calculateVelocity().y
                        scope.launch {
                            state.lazyListState.scroll {
                                with(flingBehavior) {
                                    performFling(velocity)
                                    handleOnChange(state.selectedItemRawIndex)
                                }
                            }
                        }
                    },
                )
            }
            .drawWithContent {
                drawRoundRect(
                    color = Color(0x20000000),
                    topLeft = Offset(0f, (size.height / 2) - (14.dp.toPx())),
                    size = Size(size.width, state.itemHeight),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
                drawContent()
            },
        userScrollEnabled = enabled,
        state = state.lazyListState,
        contentPadding = paddingValues,
        flingBehavior = flingBehavior,
    ) {
        items(
            count = if (state.isInfinite) Int.MAX_VALUE else items.size,
            key = { key(items[getItemsIndex(it, state, items.size)]) }
        ) { index ->
            Box(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures {
                            scope.launch {
                                state.lazyListState.animateScrollToItem(index)
                                handleOnChange(index)
                            }
                        }
                    }
                    .graphicsLayer {
                        val stepsFromSelected = index - state.selectedItemBasedOnTopRawIndex
                        val offset = state.lazyListState.firstVisibleItemScrollOffset.toFloat()
                        val pixelsFromSelected = (stepsFromSelected.toFloat() * state.itemHeight) - offset
                        val maxPixelsFromSelected = (state.viewportHeight / 2) + (state.itemHeight / 2)
                        val mult = (pixelsFromSelected / maxPixelsFromSelected)

                        scrollEffect(this, index, mult, state)
                    }
            ) {
                content(items[getItemsIndex(index, state, items.size)])
            }
        }
    }
}

typealias KMPWheelPickerScrollEffect =
        GraphicsLayerScope.(index: Int, multiplier: Float, state: KMPWheelPickerState) -> Unit

object KMPWheelPickerDefaults {
    val ScrollEffectWheel: KMPWheelPickerScrollEffect =
        fun GraphicsLayerScope.(_: Int, multiplier: Float, _: KMPWheelPickerState) {
            rotationX = 60f * multiplier
            scaleX = 1f - .5f * abs(multiplier)
            scaleY = 1f - .5f * abs(multiplier)
            alpha = 1f - .5f * abs(multiplier)
        }

    val ScrollEffectMagnify: KMPWheelPickerScrollEffect =
        fun GraphicsLayerScope.(_: Int, multiplier: Float, state: KMPWheelPickerState) {
            val scaleMult = multiplier / (1f / (((state.viewportHeight / state.itemHeight) + 1f) / 2f))
            scaleX = (1f - .6f * abs(scaleMult)).coerceAtLeast(.6f)
            scaleY = (1f - .6f * abs(scaleMult)).coerceAtLeast(.6f)
            alpha = 1f - .5f * abs(multiplier)
        }
}

private fun getItemsIndex(index: Int, state: KMPWheelPickerState, itemCount: Int) =
    if (state.isInfinite) {
        val mod = ((index - INFINITE_OFFSET) % itemCount)
        if (mod < 0) itemCount + mod else mod
    } else {
        index
    }

private const val INFINITE_OFFSET = Int.MAX_VALUE / 2