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
import androidx.compose.ui.graphics.TransformOrigin
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
import com.outsidesource.oskitkmp.lib.Platform
import com.outsidesource.oskitkmp.lib.current
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

            var index by remember { mutableStateOf(0) }

            Button(onClick = { index = 5 }) {
                Text("Hello")
            }

            Row {
                KMPWheelPicker(
                    modifier = Modifier
                        .width(100.dp)
                        .height(175.dp),
                    items = (0..105).toList(),
                    selectedIndex = index,
                    key = { it },
                    state = rememberKmpWheelPickerState(isInfinite = true, initiallySelectedItemIndex = 0),
                    onChange = {
                        println("On Change: $it")
                        index = it
                        true
                    },
                ) {
                    Box(modifier = Modifier.height(40.dp), contentAlignment = Alignment.Center) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it.toString(),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}


@Stable
data class KMPWheelPickerState(
    val initiallySelectedItemIndex: Int = 0,
    val isInfinite: Boolean = false,
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

    internal val selectedItemBasedOnTop by derivedStateOf {
        val layoutInfo = lazyListState.layoutInfo
        val item = layoutInfo.visibleItemsInfo.firstOrNull {
            it.offset + it.size - layoutInfo.viewportStartOffset > (layoutInfo.viewportSize.height / 2) - (it.size / 2)
        }
        ScrollEffectAnimationData(index = item?.index ?: initiallySelectedItemIndex, offset = item?.offset ?: 0)
    }

    internal val verticalPadding by derivedStateOf {
        val viewportHeight = lazyListState.layoutInfo.viewportSize.height.toFloat()
        val itemHeight = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.size?.toFloat() ?: 0f
        ((viewportHeight - itemHeight) / 2).coerceAtLeast(0f)
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
                    initiallySelectedItemIndex = it[1] as Int,
                )
            }
        )
    }
}

@Immutable
internal data class ScrollEffectAnimationData(
    val offset: Int,
    val index: Int,
)

@Composable
fun rememberKmpWheelPickerState(
    isInfinite: Boolean = false,
    initiallySelectedItemIndex: Int = 0,
) = rememberSaveable(initiallySelectedItemIndex, isInfinite, saver = KMPWheelPickerState.Saver()) {
    KMPWheelPickerState(
        initiallySelectedItemIndex = initiallySelectedItemIndex,
        isInfinite = isInfinite
    )
}

// TODO: Add indicators
// TODO: Support changing values but not letting the value change in an interactor and make sure the picker scrolls back

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
    state: KMPWheelPickerState = rememberKmpWheelPickerState(isInfinite = false, initiallySelectedItemIndex = 0),
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    onChange: (T) -> Unit,
    scrollEffect: KMPWheelPickerScrollEffect = KMPWheelPickerDefaults.ScrollEffectWheel,
    content: @Composable LazyItemScope.(T) -> Unit,
) {
    val isDragging = remember { VarRef(false) }
    val velocityTracker = remember { VelocityTracker() }
    val scope = rememberCoroutineScope()
    val flingBehavior = rememberSnapFlingBehavior(state.lazyListState)
    val lastOnChangeIndex = remember { VarRef(selectedIndex) }
    val scrollDebouncer = remember { Debouncer(timeoutMillis = 250, scope = scope) }

    val paddingValues = with(LocalDensity.current) {
        remember(state.verticalPadding) {
            PaddingValues(vertical = state.verticalPadding.toDp())
        }
    }

    val handleOnChange = remember(items, state) {
        handleOnChange@ { index: Int ->
            val oldIndex = lastOnChangeIndex.value
            val newValue = items[getItemsIndex(index, state, items.size)]
            if (getItemsIndex(oldIndex, state, items.size) == getItemsIndex(index, state, items.size)) return@handleOnChange
            lastOnChangeIndex.value = index

            val accepted = onChange(newValue)
        }
    }

    // Handle value changing outside WheelPicker
    LaunchedEffect(selectedIndex) {
        // If the new value equals the old value don't do anything
        if (isDragging.value || getItemsIndex(lastOnChangeIndex.value, state, items.size) == selectedIndex) return@LaunchedEffect
        lastOnChangeIndex.value = selectedIndex
        state.animateScrollToItem(selectedIndex)
    }

    LazyColumn(
        modifier = modifier
            .kmpMouseScrollFilter { _, _ ->
                if (!enabled) return@kmpMouseScrollFilter

                scrollDebouncer.emit {
                    val index = if (state.isInfinite) state.selectedItemRawIndex - INFINITE_OFFSET else state.selectedItemRawIndex
                    state.animateScrollToItem(index)
                    handleOnChange(state.selectedItemRawIndex)
                }
            }
            .pointerInput(Unit) {
                if (!enabled) return@pointerInput

                awaitEachGesture {
                    // Cancel fling on down if actively flinging
                    awaitFirstDown(requireUnconsumed = false)
                    if (!state.lazyListState.isScrollInProgress) return@awaitEachGesture
                    scope.launch { state.lazyListState.scrollBy(0f) }

                    // Adjust to correct value
                    awaitFirstUp(requireUnconsumed = false)
                    scope.launch {
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
                if (!enabled) return@pointerInput

                detectDragGestures(
                    onDragStart = {
                        isDragging.value = true
                        velocityTracker.resetTracking()
                    },
                    onDrag = { change, delta ->
                        velocityTracker.addPointerInputChange(change)
                        state.lazyListState.dispatchRawDelta(-delta.y)
                    },
                    onDragEnd = {
                        isDragging.value = false
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
                    topLeft = Offset(0f, (size.height / 2) - (state.itemHeight / 2)),
                    size = Size(size.width, state.itemHeight),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
                drawContent()
            },
        userScrollEnabled = Platform.current.isDesktop, // Allow wheel scroll if Desktop, otherwise the scrolling is handled via the pointerInput modifier
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
                        if (!enabled) return@pointerInput

                        detectTapGestures {
                            scope.launch {
                                state.lazyListState.animateScrollToItem(index)
                                handleOnChange(index)
                            }
                        }
                    }
                    .graphicsLayer {
                        val stepsFromSelected = index - state.selectedItemBasedOnTop.index
                        val offset = abs(state.selectedItemBasedOnTop.offset)
                        val pixelsFromSelected = (stepsFromSelected.toFloat() * state.itemHeight) - offset
                        val maxPixelsFromSelected = (state.viewportHeight / 2) + (state.itemHeight / 2)
                        val mult = (pixelsFromSelected / maxPixelsFromSelected).coerceIn(-1f..1f)

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
            rotationX = (90f * multiplier).coerceIn(-360f..360f)
            scaleX = (1f - .1f * abs(multiplier)).coerceIn(0f..1f)
            scaleY = (1f - .1f * abs(multiplier)).coerceIn(0f..1f)
            alpha = (1f - .5f * abs(multiplier)).coerceIn(0f..1f)
            transformOrigin = TransformOrigin(.5f, (.5f - multiplier).coerceIn(0f..1f))
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