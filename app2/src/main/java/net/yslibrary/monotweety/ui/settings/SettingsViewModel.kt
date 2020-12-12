package net.yslibrary.monotweety.ui.settings

import com.codingfeline.twitter4kt.v1.model.error.TwitterApiException
import com.codingfeline.twitter4kt.v1.model.error.TwitterError
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.yslibrary.monotweety.Config
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.data.settings.Settings
import net.yslibrary.monotweety.data.twitterapp.AppInfo
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.domain.session.Logout
import net.yslibrary.monotweety.domain.setting.GetTwitterAppByPackageName
import net.yslibrary.monotweety.domain.setting.ObserveSettings
import net.yslibrary.monotweety.domain.setting.UpdateNotificationEnabled
import net.yslibrary.monotweety.domain.setting.UpdateTimelineAppSetting
import net.yslibrary.monotweety.domain.twitterapp.GetInstalledTwitterApps
import net.yslibrary.monotweety.domain.user.FetchUser
import net.yslibrary.monotweety.domain.user.ObserveUser
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
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.time.ExperimentalTime


sealed class SettingsIntent : Intent {
    object Initialize : SettingsIntent()
    data class NotificationStateUpdated(val enabled: Boolean) : SettingsIntent()
    data class TimelineAppSelected(val packageName: String) : SettingsIntent()

    object LogoutSelected : SettingsIntent()
    object ProfileSelected : SettingsIntent()
    object PrivacyPolicySelected : SettingsIntent()
    object ChangelogSelected : SettingsIntent()
    object LicenseSelected : SettingsIntent()
    object FollowDeveloperSelected : SettingsIntent()
    object ShareAppSelected : SettingsIntent()
    object RateAppSelected : SettingsIntent()
    object GitHubSelected : SettingsIntent()
}

sealed class SettingsAction : Action {
    object Initialize : SettingsAction()

    data class SettingsUpdated(val settings: Settings) : SettingsAction()
    data class UserUpdated(val user: User) : SettingsAction()
    data class UpdateNotification(val enabled: Boolean) : SettingsAction()
    data class UpdateSelectedTimelineApp(val packageName: String) : SettingsAction()
    data class SelectedTimelineAppUpdated(val appInfo: AppInfo?) : SettingsAction()
    data class TimelineAppsUpdated(val apps: List<AppInfo>) : SettingsAction()

    object Logout : SettingsAction()
    object LogoutCompleted : SettingsAction()
    object ShareApp : SettingsAction()
    object NavigateToChangelog : SettingsAction()
    object NavigateToLicense : SettingsAction()
    data class NavigateToExternalApp(val url: String) : SettingsAction()

    data class UserFetchFailed(val error: Throwable) : SettingsAction()
}

sealed class SettingsEffect : Effect {
    object ToLicense : SettingsEffect()
    object ToChangelog : SettingsEffect()
    object ToSplash : SettingsEffect()
    data class ShareApp(val url: String) : SettingsEffect()
    data class OpenBrowser(val url: String) : SettingsEffect()
    data class UpdateNotification(val enabled: Boolean) : SettingsEffect()

    data class ShowError(val message: String?) : SettingsEffect()
}

data class SettingsState(
    val state: ULIEState,
    val settings: Settings?,
    val user: User?,
    val selectedTimelineApp: AppInfo?,
    val timelineApps: List<AppInfo>,
) : State {
    companion object {
        fun initialState(): SettingsState {
            return SettingsState(
                state = ULIEState.UNINITIALIZED,
                settings = null,
                user = null,
                selectedTimelineApp = null,
                timelineApps = emptyList(),
            )
        }
    }
}

class SettingsProcessor @Inject constructor(
    private val observeSettings: ObserveSettings,
    private val observeUser: ObserveUser,
    private val getInstalledTwitterApps: GetInstalledTwitterApps,
    private val getTwitterAppByPackageName: GetTwitterAppByPackageName,
    private val fetchUser: FetchUser,
    private val logout: Logout,
    private val updateNotificationEnabled: UpdateNotificationEnabled,
    private val updateTimelineAppSetting: UpdateTimelineAppSetting,
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
                doGetInstalledTwitterApps()
            }
            is SettingsAction.UpdateNotification -> {
                launch { updateNotificationEnabled(action.enabled) }
            }
            SettingsAction.Logout -> {
                launch {
                    logout()
                    put(SettingsAction.LogoutCompleted)
                }
            }
            is SettingsAction.UpdateSelectedTimelineApp -> {
                launch {
                    updateTimelineAppSetting(
                        enabled = action.packageName.isNotEmpty(),
                        packageName = action.packageName)
                }
            }
            is SettingsAction.UserUpdated,
            is SettingsAction.SettingsUpdated,
            SettingsAction.ShareApp,
            SettingsAction.NavigateToChangelog,
            SettingsAction.NavigateToLicense,
            is SettingsAction.NavigateToExternalApp,
            SettingsAction.LogoutCompleted,
            is SettingsAction.SelectedTimelineAppUpdated,
            is SettingsAction.TimelineAppsUpdated,
            -> {
                // no-op
            }
        }
    }

    private fun doObserveSetting() {
        observeSettings()
            .onEach { setting -> put(SettingsAction.SettingsUpdated(setting)) }
            .onEach {
                val appInfo = getTwitterAppByPackageName(it.timelineAppPackageName)
                put(SettingsAction.SelectedTimelineAppUpdated(appInfo))
            }
            .launchIn(this)
    }

    private fun doObserveUser() {
        observeUser()
            .onEach { user ->
                if (user.isValid(clock)) {
                    put(SettingsAction.UserUpdated(user))
                } else {
                    try {
                        fetchUser()
                    } catch (e: Throwable) {
                        Timber.e(e, "failed to fetch user: ${e.message}")
                        put((SettingsAction.UserFetchFailed(e)))
                        logoutIfNeeded(e)
                    }
                }
            }
            .launchIn(this)
    }

    private fun doGetInstalledTwitterApps() {
        launch {
            val apps = getInstalledTwitterApps()
            put(SettingsAction.TimelineAppsUpdated(apps))
        }
    }

    private suspend fun logoutIfNeeded(error: Throwable) {
        if (error is TwitterApiException) {
            if (error.contains(TwitterError.Code.CouldNotAuthenticate)) {
                logout()
                put(SettingsAction.LogoutCompleted)
            }
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
    private val config: Config,
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
            is SettingsIntent.NotificationStateUpdated ->
                SettingsAction.UpdateNotification(intent.enabled)
            SettingsIntent.PrivacyPolicySelected -> SettingsAction.NavigateToExternalApp(config.privacyPolicyUrl)
            SettingsIntent.ChangelogSelected -> SettingsAction.NavigateToChangelog
            SettingsIntent.LicenseSelected -> SettingsAction.NavigateToLicense
            SettingsIntent.FollowDeveloperSelected -> SettingsAction.NavigateToExternalApp(config.twitterUrl)
            SettingsIntent.ShareAppSelected -> SettingsAction.ShareApp
            SettingsIntent.RateAppSelected -> SettingsAction.NavigateToExternalApp(config.googlePlayUrl)
            SettingsIntent.GitHubSelected -> SettingsAction.NavigateToExternalApp(config.githubUrl)
            SettingsIntent.LogoutSelected -> SettingsAction.Logout
            SettingsIntent.ProfileSelected -> {
                if (state.user == null) {
                    GlobalAction.NoOp
                } else {
                    SettingsAction.NavigateToExternalApp("https://twitter.com/${state.user.screenName}")
                }
            }
            is SettingsIntent.TimelineAppSelected -> SettingsAction.UpdateSelectedTimelineApp(intent.packageName)
        }
    }

    override fun reduce(previousState: SettingsState, action: SettingsAction): SettingsState {
        return when (action) {
            SettingsAction.Initialize -> {
                previousState.copy(state = ULIEState.LOADING)
            }
            is SettingsAction.SettingsUpdated -> {
                if (previousState.settings?.notificationEnabled != action.settings.notificationEnabled) {
                    sendEffect(SettingsEffect.UpdateNotification(action.settings.notificationEnabled))
                }
                previousState.copy(settings = action.settings)
            }
            is SettingsAction.UserUpdated -> {
                previousState.copy(
                    state = ULIEState.IDLE,
                    user = action.user,
                )
            }
            is SettingsAction.UpdateNotification -> previousState
            is SettingsAction.NavigateToExternalApp -> {
                sendEffect(SettingsEffect.OpenBrowser(action.url))
                previousState
            }
            SettingsAction.NavigateToChangelog -> {
                sendEffect(SettingsEffect.ToChangelog)
                previousState
            }
            SettingsAction.NavigateToLicense -> {
                sendEffect(SettingsEffect.ToLicense)
                previousState
            }
            SettingsAction.ShareApp -> {
                sendEffect(SettingsEffect.ShareApp(config.googlePlayUrl))
                previousState
            }
            SettingsAction.Logout -> {
                previousState
            }
            SettingsAction.LogoutCompleted -> {
                sendEffect(SettingsEffect.ToSplash)
                previousState
            }
            is SettingsAction.SelectedTimelineAppUpdated -> {
                previousState.copy(
                    selectedTimelineApp = action.appInfo
                )
            }
            is SettingsAction.TimelineAppsUpdated -> {
                previousState.copy(timelineApps = action.apps)
            }
            is SettingsAction.UpdateSelectedTimelineApp -> {
                previousState
            }
            is SettingsAction.UserFetchFailed -> {
                val message = if (action.error is TwitterApiException) {
                    action.error.errors.firstOrNull()?.message
                } else {
                    action.error.message
                }
                sendEffect(SettingsEffect.ShowError(message))
                previousState
            }
        }
    }

}
