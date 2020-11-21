package net.yslibrary.monotweety.notification

import com.codingfeline.twitter4kt.core.isSuccess
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.data.twitterapp.AppInfo
import net.yslibrary.monotweety.domain.setting.GetTwitterAppByPackageName
import net.yslibrary.monotweety.domain.setting.ObserveSettings
import net.yslibrary.monotweety.domain.status.BuildAndValidateStatusString
import net.yslibrary.monotweety.domain.status.UpdateStatus
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

    object OpenTweetDialog : NotificationIntent()
    data class Tweet(val status: String) : NotificationIntent()
}

sealed class NotificationAction : Action {
    object Initialize : NotificationAction()

    data class Tweet(val status: String) : NotificationAction()
    object TweetCompleted : NotificationAction()
    data class StatusTooLong(val status: String) : NotificationAction()
    data class TweetFailed(val error: Throwable) : NotificationAction()

    object OpenTweetDialog : NotificationAction()

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

    object OpenTweetDialog : NotificationEffect()
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
    private val buildAndValidateStatusString: BuildAndValidateStatusString,
    private val updateStatus: UpdateStatus,
    dispatchers: CoroutineDispatchers,
) : Processor<NotificationAction>(
    dispatchers = dispatchers,
) {
    override fun processAction(action: NotificationAction) {
        when (action) {
            NotificationAction.Initialize -> {
                doObserveSettings()
            }
            is NotificationAction.Tweet -> {
                launch {
                    val result = buildAndValidateStatusString(action.status)
                    if (result.isValid) {
                        val apiResult = updateStatus(result.status)
                        if (apiResult.isSuccess()) {
                            put(NotificationAction.TweetCompleted)
                        } else {
                            put(NotificationAction.TweetFailed(apiResult.error))
                        }
                    } else {
                        // should pass a status string without footer(footer should be added in Editor dialog)
                        put(NotificationAction.StatusTooLong(action.status))
                    }
                }
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
            NotificationIntent.OpenTweetDialog -> NotificationAction.OpenTweetDialog
            is NotificationIntent.Tweet -> NotificationAction.Tweet(intent.status)
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
            is NotificationAction.Tweet -> previousState
            NotificationAction.TweetCompleted -> {
                sendEffect(NotificationEffect.UpdateCompleted)
                previousState
            }
            NotificationAction.OpenTweetDialog -> {
                sendEffect(NotificationEffect.OpenTweetDialog)
                previousState
            }
            is NotificationAction.StatusTooLong -> {
                sendEffect(NotificationEffect.StatusTooLong(action.status))
                previousState
            }
            is NotificationAction.TweetFailed -> {
                sendEffect(NotificationEffect.Error(action.error.message ?: ""))
                previousState
            }
        }
    }

    public override fun onCleared() {
        super.onCleared()
    }
}

