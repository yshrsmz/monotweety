package net.yslibrary.monotweety.notification

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.data.twitterapp.AppInfo
import net.yslibrary.monotweety.domain.setting.GetTwitterAppByPackageName
import net.yslibrary.monotweety.domain.setting.ObserveSettings
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import net.yslibrary.monotweety.ui.arch.ULIEState
import javax.inject.Inject

sealed class NotificationIntent : Intent {
    object Initialize : NotificationIntent()

}

sealed class NotificationAction : Action {
    object Initialize : NotificationAction()

    data class FooterSettingsUpdated(
        val enabled: Boolean,
        val text: String,
    ) : NotificationAction()

    data class TimelineAppUpdated(
        val enabled: Boolean,
        val appInfo: AppInfo?,
    ) : NotificationAction()
}

sealed class NotificationEffect : Effect {
    object UpdateCompleted : NotificationEffect()
    data class Error(val message: String) : NotificationEffect()
    data class StatusTooLong(val status: String) : NotificationEffect()

    object StopNotification : NotificationEffect()
}

data class NotificationState(
    val state: ULIEState,
    val footerEnabled: Boolean,
    val footerText: String,
    val timelineAppEnabled: Boolean,
    val timelineApp: AppInfo?,
) : State {
    companion object {
        fun initialState(): NotificationState {
            return NotificationState(
                state = ULIEState.UNINITIALIZED,
                footerEnabled = false,
                footerText = "",
                timelineAppEnabled = false,
                timelineApp = null,
            )
        }
    }
}

class NotificationProcessor @Inject constructor(
    private val observeSettings: ObserveSettings,
    private val getTwitterAppByPackageName: GetTwitterAppByPackageName,
    dispatchers: CoroutineDispatchers,
) : Processor<NotificationAction>(
    dispatchers = dispatchers,
) {
    override fun processAction(action: NotificationAction) {
        when (action) {
            NotificationAction.Initialize -> {
                doObserveSettings()
            }
        }
    }

    private fun doObserveSettings() {
        observeSettings()
            .onEach {
                put(NotificationAction.FooterSettingsUpdated(
                    enabled = it.footerEnabled,
                    text = it.footerText))
            }
            .onEach {
                val appInfo = getTwitterAppByPackageName(it.timelineAppPackageName)
                put(NotificationAction.TimelineAppUpdated(
                    enabled = it.timelineAppEnabled,
                    appInfo = appInfo))
            }
            .launchIn(this)
    }
}

class NotificationViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    processor: NotificationProcessor,
) : MviViewModel<NotificationIntent, NotificationAction, NotificationState, NotificationEffect>(
    initialState = NotificationState.initialState(),
    dispatchers = dispatchers,
    processor = processor,
) {
    override fun intentToAction(intent: NotificationIntent, state: NotificationState): Action {
        return when (intent) {
            NotificationIntent.Initialize -> NotificationAction.Initialize
        }
    }

    override fun reduce(
        previousState: NotificationState,
        action: NotificationAction,
    ): NotificationState {
        return when (action) {
            NotificationAction.Initialize -> {
                previousState.copy(state = ULIEState.LOADING)
            }
            is NotificationAction.FooterSettingsUpdated -> {
                previousState.copy(
                    state = ULIEState.IDLE,
                    footerEnabled = action.enabled,
                    footerText = action.text,
                )
            }
            is NotificationAction.TimelineAppUpdated -> {
                previousState.copy(
                    state = ULIEState.IDLE,
                    timelineAppEnabled = action.enabled,
                    timelineApp = action.appInfo,
                )
            }
        }
    }

    public override fun onCleared() {
        super.onCleared()
    }
}

