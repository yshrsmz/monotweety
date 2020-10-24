package net.yslibrary.monotweety.ui.settings

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.data.settings.Settings
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.domain.setting.ObserveSetting
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import net.yslibrary.monotweety.ui.arch.ULIEState
import javax.inject.Inject


sealed class SettingsIntent : Intent {
    object Initialize : SettingsIntent()
}

sealed class SettingsAction : Action {
    object Initialize : SettingsAction()
    data class SettingUpdated(val settings: Settings) : SettingsAction()
}

sealed class SettingsEffect : Effect

data class SettingsState(
    val state: ULIEState,
    val settings: Settings?,
    val user: User?,
) : State {
    companion object {
        fun initialState(): SettingsState {
            return SettingsState(
                state = ULIEState.UNINITIALIZED,
                settings = null,
                user = null,
            )
        }
    }
}

class SettingsProcessor @Inject constructor(
    private val observeSetting: ObserveSetting,
    dispatchers: CoroutineDispatchers,
) : Processor<SettingsAction>(
    dispatchers = dispatchers
) {
    override fun processAction(action: SettingsAction) {
        when (action) {
            SettingsAction.Initialize -> {
                doObserveSetting()
            }
            is SettingsAction.SettingUpdated -> {
                // no-op
            }
        }
    }

    private fun doObserveSetting() {
        launch {
            observeSetting()
                .onEach { setting -> put(SettingsAction.SettingUpdated(setting)) }
                .collect()
        }
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
        return when (intent) {
            SettingsIntent.Initialize -> SettingsAction.Initialize
        }
    }

    override fun reduce(previousState: SettingsState, newAction: SettingsAction): SettingsState {
        return when (newAction) {
            SettingsAction.Initialize -> {
                previousState.copy(state = ULIEState.LOADING)
            }
            is SettingsAction.SettingUpdated -> {
                previousState.copy(settings = newAction.settings)
            }
        }
    }

}
