package net.yslibrary.monotweety.ui.settings

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.data.settings.Settings
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.domain.setting.ObserveSettings
import net.yslibrary.monotweety.domain.user.FetchUser
import net.yslibrary.monotweety.domain.user.ObserveUser
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import net.yslibrary.monotweety.ui.arch.ULIEState
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.time.ExperimentalTime


sealed class SettingsIntent : Intent {
    object Initialize : SettingsIntent()
}

sealed class SettingsAction : Action {
    object Initialize : SettingsAction()
    data class SettingsUpdated(val settings: Settings) : SettingsAction()
    data class UserUpdated(val user: User) : SettingsAction()
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
    private val observeSettings: ObserveSettings,
    private val observeUser: ObserveUser,
    private val fetchUser: FetchUser,
    private val clock: Clock,
    dispatchers: CoroutineDispatchers,
) : Processor<SettingsAction>(
    dispatchers = dispatchers
) {
    override fun processAction(action: SettingsAction) {
        when (action) {
            SettingsAction.Initialize -> {
                doObserveSetting()
                doObserveUser()
            }
            is SettingsAction.SettingsUpdated -> {
                // no-op
            }
        }
    }

    private fun doObserveSetting() {
        launch {
            observeSettings()
                .onEach { setting -> put(SettingsAction.SettingsUpdated(setting)) }
                .collect()
        }
    }

    private fun doObserveUser() {
        launch {
            observeUser()
                .onEach { user ->
                    if (user.isValid(clock)) {
                        put(SettingsAction.UserUpdated(user))
                    } else {
                        fetchUser()
                    }
                }
                .collect()
        }
    }
}

@OptIn(ExperimentalTime::class, ExperimentalContracts::class)
private fun User?.isValid(clock: Clock): Boolean {
    contract {
        returns(true) implies (this@isValid != null)
    }
    if (this == null) return false
    val updated = Instant.fromEpochSeconds(updatedAt)
    val duration = clock.now() - updated
    return duration.inHours < 12
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
            is SettingsAction.SettingsUpdated -> {
                previousState.copy(settings = newAction.settings)
            }
            is SettingsAction.UserUpdated -> {
                previousState.copy(user = newAction.user)
            }
        }
    }

}
