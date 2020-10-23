package net.yslibrary.monotweety.ui.settings

import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import net.yslibrary.monotweety.ui.arch.ULIEState
import javax.inject.Inject


sealed class SettingsIntent : Intent
sealed class SettingsAction : Action
sealed class SettingsEffect : Effect

data class SettingsState(
    val state: ULIEState,
) : State {
    companion object {
        fun initialState(): SettingsState {
            return SettingsState(
                state = ULIEState.UNINITIALIZED,
            )
        }
    }
}

class SettingsProcessor @Inject constructor(
    dispatchers: CoroutineDispatchers,
) : Processor<SettingsAction>(
    dispatchers = dispatchers
) {
    override fun processAction(action: SettingsAction) {
        TODO("Not yet implemented")
    }
}

class SettingsViewModel @Inject constructor(
    processor: SettingsProcessor,
    dispatchers: CoroutineDispatchers,
) : MviViewModel<SettingsIntent, SettingsAction, SettingsState, SettingsEffect>(
    initialState = SettingsState.initialState(),
    processor = processor,
    dispatchers = dispatchers,
) {
    override fun intentToAction(intent: SettingsIntent, state: SettingsState): Action {
        TODO("Not yet implemented")
    }

    override fun reduce(previousState: SettingsState, newAction: SettingsAction): SettingsState {
        TODO("Not yet implemented")
    }

}
