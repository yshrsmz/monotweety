package net.yslibrary.monotweety.ui.footereditor

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.data.settings.Settings
import net.yslibrary.monotweety.domain.setting.ObserveSettings
import net.yslibrary.monotweety.domain.setting.UpdateFooterSettings
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.GlobalAction
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import net.yslibrary.monotweety.ui.arch.ULIEState
import timber.log.Timber
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

    data class Save(val enabled: Boolean, val footer: String) : FooterEditorAction()
    object Close : FooterEditorAction()
}

sealed class FooterEditorEffect : Effect {
    object Close : FooterEditorEffect()
}

data class FooterEditorState(
    val state: ULIEState,
    val settings: Settings?,
    val footerText: String,
    val footerEnabled: Boolean,
    val isValidFooterText: Boolean,
) : State {
    companion object {
        fun initialState(): FooterEditorState {
            return FooterEditorState(
                state = ULIEState.UNINITIALIZED,
                settings = null,
                footerText = "",
                footerEnabled = false,
                isValidFooterText = true
            )
        }
    }
}

class FooterEditorProcessor @Inject constructor(
    private val observeSettings: ObserveSettings,
    private val updateFooterSettings: UpdateFooterSettings,
    dispatchers: CoroutineDispatchers,
) : Processor<FooterEditorAction>(dispatchers) {
    override fun processAction(action: FooterEditorAction) {
        Timber.d("processAction: $action")
        when (action) {
            FooterEditorAction.Initialize -> {
                doObserveSettings()
            }
            is FooterEditorAction.Save -> {
                launch {
                    updateFooterSettings(action.enabled, action.footer)
                    put(FooterEditorAction.Close)
                }
            }
            is FooterEditorAction.SettingsUpdated,
            is FooterEditorAction.EnableStateUpdated,
            is FooterEditorAction.FooterTextUpdated,
            FooterEditorAction.Close,
            -> {
                // no-op
            }
        }
    }

    private fun doObserveSettings() {
        observeSettings()
            .onEach { put(FooterEditorAction.SettingsUpdated(it)) }
            .launchIn(this)
    }
}

class FooterEditorViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    processor: FooterEditorProcessor,
) : MviViewModel<FooterEditorIntent, FooterEditorAction, FooterEditorState, FooterEditorEffect>(
    initialState = FooterEditorState.initialState(),
    dispatchers = dispatchers,
    processor = processor,
) {
    override fun intentToAction(intent: FooterEditorIntent, state: FooterEditorState): Action {
        return when (intent) {
            FooterEditorIntent.Initialize -> FooterEditorAction.Initialize
            is FooterEditorIntent.EnableStateUpdated ->
                FooterEditorAction.EnableStateUpdated(intent.enabled)
            is FooterEditorIntent.FooterTextUpdated ->
                FooterEditorAction.FooterTextUpdated(intent.value)
            FooterEditorIntent.SaveRequested -> {
                when {
                    state.isValidFooterText ->
                        FooterEditorAction.Save(state.footerEnabled, state.footerText)
                    else -> GlobalAction.NoOp
                }
            }
            FooterEditorIntent.CancelRequested -> FooterEditorAction.Close
        }
    }

    override fun reduce(
        previousState: FooterEditorState,
        action: FooterEditorAction,
    ): FooterEditorState {
        Timber.d("reduce: $action")
        return when (action) {
            FooterEditorAction.Initialize -> {
                previousState.copy(state = ULIEState.LOADING)
            }
            is FooterEditorAction.SettingsUpdated -> {
                val initialLoad = previousState.state == ULIEState.LOADING ||
                        previousState.settings == null
                val footerEnabled: Boolean
                val footerText: String
                if (initialLoad) {
                    footerEnabled = action.settings?.footerEnabled ?: false
                    footerText = action.settings?.footerText ?: ""
                } else {
                    footerEnabled = previousState.footerEnabled
                    footerText = previousState.footerText
                }

                previousState.copy(
                    state = ULIEState.IDLE,
                    settings = action.settings,
                    footerEnabled = footerEnabled,
                    footerText = footerText,
                    isValidFooterText = isValidFooter(footerText),
                )
            }
            is FooterEditorAction.EnableStateUpdated -> {
                previousState.copy(
                    footerEnabled = action.enabled
                )
            }
            is FooterEditorAction.FooterTextUpdated -> {
                previousState.copy(
                    footerText = action.value,
                    isValidFooterText = isValidFooter(action.value)
                )
            }
            is FooterEditorAction.Save -> previousState
            FooterEditorAction.Close -> {
                sendEffect(FooterEditorEffect.Close)
                previousState
            }
        }
    }

    private fun isValidFooter(text: String): Boolean {
        return !text.contains('@')
    }
}
