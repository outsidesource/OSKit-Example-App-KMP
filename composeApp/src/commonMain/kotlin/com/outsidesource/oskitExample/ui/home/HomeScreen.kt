package com.outsidesource.oskitExample.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.pointer.awaitFirstUp
import kotlinx.coroutines.delay
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

            val wheelPickerState = rememberWheelPickerState(isInfinite = true)

            WheelPicker(
                modifier = Modifier
                    .width(100.dp)
                    .height(175.dp),
                items = (0..105).toList(),
                key = { it },
                state = wheelPickerState,
                onChange = {
                    println("On Change: $it")
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
data class WheelPickerState(
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
        fun Saver(): Saver<WheelPickerState, *> = Saver(
            save = { listOf(it.isInfinite, it.selectedItemRawIndex) },
            restore = {
                WheelPickerState(
                    isInfinite = it[0] as Boolean,
                    initiallySelectedItemIndex = it[1] as Int
                )
            }
        )
    }
}

@Composable
fun rememberWheelPickerState(
    isInfinite: Boolean = true,
    initiallySelectedItemIndex: Int = 0,
) = rememberSaveable(initiallySelectedItemIndex, isInfinite, saver = WheelPickerState.Saver()) {
    WheelPickerState(
        initiallySelectedItemIndex = initiallySelectedItemIndex,
        isInfinite = isInfinite
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> WheelPicker(
    items: List<T>,
    key: (T) -> Any,
    state: WheelPickerState,
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    onChange: (T) -> Unit,
    scrollEffect: WheelPickerScrollEffect = WheelPickerDefaults.ScrollEffectWheel,
    content: @Composable LazyItemScope.(T) -> Unit,
) {
    val velocityTracker = remember { VelocityTracker() }
    val scope = rememberCoroutineScope()
    val flingBehavior = rememberSnapFlingBehavior(state.lazyListState)

    val paddingValues = with(LocalDensity.current) {
        remember(state.verticalPadding) {
            PaddingValues(vertical = state.verticalPadding.toDp())
        }
    }

    // TODO: Both pointerInputs should only happen on desktop, but need to make sure that scroll cancellation and letting go still adjusts
    // TODO: Add scroll wheel debouncer to fix scroll after it's done or disable scroll
    // TODO: Need to add indicators
    // TODO: Add alpha mask
    // TODO: animation with mult only when item starts to enter selection
    LazyColumn(
        modifier = modifier
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
                                onChange(items[getItemsIndex(state.selectedItemRawIndex, state, items)])
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
                                    onChange(items[getItemsIndex(state.selectedItemRawIndex, state, items)])
                                }
                            }
                        }
                    },
                )
            }
            .drawWithContent {
                drawRoundRect(
                    color = Color(0x22000000),
                    topLeft = Offset(0f, (size.height / 2) - (14f * density)),
                    size = Size(size.width, 25 * density),
                    cornerRadius = CornerRadius(20f, 20f)
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
            key = { key(items[getItemsIndex(it, state, items)]) }
        ) { index ->
            Box(
                modifier = Modifier
//                    .pointerInput(Unit) {
//                        detectTapGestures {
//                            scope.launch {
//                                // TODO: This is wrong
//                                state.lazyListState.animateScrollToItem(getInfiniteIndex(index, state, items))
//                            }
//                        }
//                    }
                    .graphicsLayer {
                        val stepsFromSelected = index - state.selectedItemBasedOnTopRawIndex
                        val offset = state.lazyListState.firstVisibleItemScrollOffset.toFloat()
                        val pixelsFromSelected = (stepsFromSelected.toFloat() * state.itemHeight) - offset
                        val maxPixelsFromSelected = (state.viewportHeight / 2) + (state.itemHeight / 2)
                        val mult = (pixelsFromSelected / maxPixelsFromSelected)

                        scrollEffect(this, index, mult, state)
                    }
            ) {
                content(items[getItemsIndex(index, state, items)])
            }
        }
    }
}

typealias WheelPickerScrollEffect = GraphicsLayerScope.(index: Int, multiplier: Float, state: WheelPickerState) -> Unit

object WheelPickerDefaults {
    val ScrollEffectWheel: WheelPickerScrollEffect =
        fun GraphicsLayerScope.(_: Int, multiplier: Float, _: WheelPickerState) {
            rotationX = 60f * multiplier
            scaleX = 1f - .5f * abs(multiplier)
            scaleY = 1f - .5f * abs(multiplier)
            alpha = 1f - .5f * abs(multiplier)
        }

    val ScrollEffectMagnify: WheelPickerScrollEffect =
        fun GraphicsLayerScope.(_: Int, multiplier: Float, state: WheelPickerState) {
            val scaleMult = multiplier / (1f / (((state.viewportHeight / state.itemHeight) + 1f) / 2f))
            scaleX = (1f - .6f * abs(scaleMult)).coerceAtLeast(.6f)
            scaleY = (1f - .6f * abs(scaleMult)).coerceAtLeast(.6f)
            alpha = 1f - .5f * abs(multiplier)
        }
}

private fun <T> getItemsIndex(index: Int, state: WheelPickerState, items: List<T>) =
    if (state.isInfinite) {
        val mod = ((index - INFINITE_OFFSET) % items.size)
        if (mod < 0) items.size + mod else mod
    } else {
        index
    }

private const val INFINITE_OFFSET = Int.MAX_VALUE / 2