package net.yslibrary.monotweety.ui.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

interface Intent
interface Action
interface State
interface Effect

interface GlobalAction : Action


@OptIn(ExperimentalCoroutinesApi::class)
abstract class MviViewModel<INTENT : Intent, ACTION : Action, STATE : State, EFFECT : Effect>(
    initialState: STATE
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
        }
    }

    private fun initReducer(initialState: STATE) {
        _actions.consumeAsFlow()
            .scan(initialState) { previousState, action ->
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
            .onEach { newState -> _states.value = newState }
            .launchIn(viewModelScope)
    }

    protected abstract fun intentToAction(intent: INTENT, state: STATE): ACTION

    protected abstract fun reduce(previousState: STATE, newAction: ACTION): STATE

    init {
        initReducer(initialState)
    }
}
