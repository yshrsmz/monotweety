package net.yslibrary.monotweety.ui.footereditor

import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.data.settings.Settings
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import net.yslibrary.monotweety.ui.arch.ULIEState
import javax.inject.Inject

sealed class FooterEditorIntent : Intent {
    object Initialize : FooterEditorIntent()

    data class EnableStateUpdated(val enabled: Boolean) : FooterEditorIntent()
    data class FooterTextUpdated(val value: String) : FooterEditorIntent()

    object SaveRequested : FooterEditorIntent()
    object CancelRequested : FooterEditorIntent()
}

sealed class FooterEditorAction : Action {
    object Initialize : FooterEditorAction()

    data class SettingsUpdated(val settings: Settings?) : FooterEditorAction()
    data class EnableStateUpdated(val enabled: Boolean) : FooterEditorAction()
    data class FooterTextUpdated(val value: String) : FooterEditorAction()

    object Save : FooterEditorAction()
    object Close : FooterEditorAction()
}

sealed class FooterEditorEffect : Effect {

}

data class FooterEditorState(
    val state: ULIEState,
    val settings: Settings?,
    val text: String,
    val footerEnabled: Boolean,
) : State {
    companion object {
        fun initialState(): FooterEditorState {
            return FooterEditorState(
                state = ULIEState.UNINITIALIZED,
                settings = null,
                text = "",
                footerEnabled = false,
            )
        }
    }
}

class FooterEditorProcessor @Inject constructor(
    dispatchers: CoroutineDispatchers,
) : Processor<FooterEditorAction>(dispatchers) {
    override fun processAction(action: FooterEditorAction) {

    }
}

class FooterEditorViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
) : MviViewModel<FooterEditorIntent, FooterEditorAction, FooterEditorState, FooterEditorEffect>(
    initialState = FooterEditorState.initialState(),
    dispatchers = dispatchers,
) {
    override fun intentToAction(intent: FooterEditorIntent, state: FooterEditorState): Action {
        return when (intent) {
            FooterEditorIntent.Initialize -> FooterEditorAction.Initialize
            is FooterEditorIntent.EnableStateUpdated ->
                FooterEditorAction.EnableStateUpdated(intent.enabled)
            is FooterEditorIntent.FooterTextUpdated ->
                FooterEditorAction.FooterTextUpdated(intent.value)
            FooterEditorIntent.SaveRequested -> TODO()
            FooterEditorIntent.CancelRequested -> TODO()
        }
    }

    override fun reduce(
        previousState: FooterEditorState,
        action: FooterEditorAction,
    ): FooterEditorState {
        TODO("Not yet implemented")
    }
}
