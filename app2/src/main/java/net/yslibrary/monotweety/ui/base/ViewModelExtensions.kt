package net.yslibrary.monotweety.ui.base

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.State

inline fun <E : Effect, VM : MviViewModel<*, *, *, E>> VM.consumeEffects(
    scope: LifecycleCoroutineScope,
    crossinline effectHandler: (effect: E) -> Unit,
) {
    scope.launchWhenStarted {
        effects.onEach { effectHandler(it) }
            .collect()
    }
}

inline fun <S : State, VM : MviViewModel<*, *, S, *>> VM.consumeStates(
    scope: LifecycleCoroutineScope,
    crossinline stateHandler: (state: S) -> Unit,
) {
    scope.launchWhenStarted {
        states.onEach { stateHandler(it) }
            .collect()
    }
}
