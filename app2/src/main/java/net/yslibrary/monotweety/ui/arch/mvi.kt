package net.yslibrary.monotweety.ui.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import net.yslibrary.monotweety.base.CoroutineDispatchers
import kotlin.coroutines.CoroutineContext

interface Intent
interface Action
interface State
interface Effect

interface GlobalAction : Action

abstract class Processor<ACTION : Action>(
    private val dispatchers: CoroutineDispatchers
) : CoroutineScope {
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext = dispatchers.background + job

    private var postActionCallback: ((action: ACTION) -> Unit)? = null

    abstract fun processAction(action: ACTION)

    protected fun put(action: ACTION) = launch(dispatchers.main) {
        postActionCallback?.invoke(action)
    }

    fun setPostActionCallback(callback: (action: ACTION) -> Unit) {
        postActionCallback = callback
    }

    fun onCleared() {
        coroutineContext.cancel()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
abstract class MviViewModel<INTENT : Intent, ACTION : Action, STATE : State, EFFECT : Effect>(
    initialState: STATE,
    private val dispatchers: CoroutineDispatchers,
    private val processor: Processor<ACTION> = NoOpProcessor(dispatchers),
) : ViewModel() {

    private val _states = MutableStateFlow(initialState)
    val state: STATE get() = _states.value

    private val _effects = MutableSharedFlow<EFFECT>()
    val effects: Flow<EFFECT> get() = _effects

    private val _actions = Channel<Any>(Channel.BUFFERED)

    fun dispatch(intent: INTENT) {
        val action = intentToAction(intent, state)
        processAction(action)
    }

    private fun processAction(action: ACTION) {
        viewModelScope.launch {
            _actions.send(action)
            processor.processAction(action)
        }
    }

    private fun initReducer(initialState: STATE) {
        _actions.consumeAsFlow()
            .scan(initialState) { previousState, action ->
                @Suppress("MemberVisibilityCanBePrivate")
                when (action) {
                    is GlobalAction -> {
                        // TODO: handle GlobalAction
                        previousState
                    }
                    else -> {
                        @Suppress("UNCHECKED_CAST")
                        (action as? ACTION)?.let { a -> reduce(previousState, a) }
                            ?: run { previousState }
                    }
                }
            }
            .flowOn(dispatchers.background)
            .onEach { newState -> _states.value = newState }
            .launchIn(viewModelScope)
    }

    protected abstract fun intentToAction(intent: INTENT, state: STATE): ACTION

    protected abstract fun reduce(previousState: STATE, newAction: ACTION): STATE

    init {
        processor.setPostActionCallback(::processAction)
        initReducer(initialState)
    }

    class NoOpProcessor<ACTION : Action>(dispatchers: CoroutineDispatchers) :
        Processor<ACTION>(dispatchers) {
        override fun processAction(action: ACTION) {
            // no-op
        }
    }
}
