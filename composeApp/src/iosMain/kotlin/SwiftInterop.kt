import com.outsidesource.oskitkmp.outcome.Outcome
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlin.coroutines.cancellation.CancellationException


sealed class SwiftOutcome<out V, out E>(private val outcome: Outcome<V, E>) {
    data class Ok<out V, out E>(val value: V) : SwiftOutcome<V, E>(outcome = Outcome.Ok(value))
    data class Error<out V, out E>(val error: E) : SwiftOutcome<V, E>(outcome = Outcome.Error(error))

    fun unwrap() = outcome
}


// TODO: Might be able to add a heartbeat or something to check if the flow is still active
/**
 * SwiftFlow
 *
 * Allows creation of a cold flow in Swift. Supports cancellation but is only checked when trying to emit or by calling
 * ensureActive()
 */
abstract class SwiftFlow<T : Any> {
    private var scope: ProducerScope<T>? = null

    @Throws(SwiftFlowCancellationException::class, CancellationException::class)
    protected suspend fun emit(value: T) {
        if (scope?.isActive == false) throw SwiftFlowCancellationException()
        scope?.send(value)
    }

    @Throws(SwiftFlowCancellationException::class, CancellationException::class)
    protected suspend fun ensureActive() {
        if (scope?.isActive == false) throw SwiftFlowCancellationException()
    }

    abstract suspend fun create()

    fun unwrap(): Flow<T> = channelFlow {
        scope = this
        try {
            create()
        } catch (e: SwiftFlowCancellationException) {
            // Do nothing
        }
    }.onCompletion {
        scope = null
    }
}

class SwiftMutableSharedFlow<T : Any>(
    replay: Int = 0,
    extraBufferCapacity: Int = 0,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND
) {
    private val flow = MutableSharedFlow<T>(replay, extraBufferCapacity, onBufferOverflow)
    fun unwrap(): MutableSharedFlow<T> = flow
}

class SwiftMutableStateFlow<T : Any>(value: T) {
    private val flow = MutableStateFlow(value)
    fun unwrap(): MutableStateFlow<T> = flow
}

class SwiftFlowCancellationException : CancellationException("SwiftFlow has been cancelled")