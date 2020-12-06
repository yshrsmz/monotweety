package net.yslibrary.monotweety.notification

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.codingfeline.twitter4kt.core.model.oauth1a.AccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.data.session.toAccessToken
import net.yslibrary.monotweety.domain.session.ObserveSession
import net.yslibrary.monotweety.ui.arch.ULIEState
import net.yslibrary.monotweety.ui.base.consumeEffects
import net.yslibrary.monotweety.ui.base.consumeStates
import net.yslibrary.monotweety.ui.compose.ComposeActivity
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.main.MainActivity
import java.util.Locale
import javax.inject.Inject

class NotificationService : LifecycleService(), HasComponent<NotificationServiceComponent> {

    private val observeSession: ObserveSession by lazy {
        App.appComponent(this).observeSession()
    }

    override val component by lazy {
        val token =
            requireNotNull(accessToken) { "AccessToken is required to create a UserComponent" }
        App.getOrCreateUserComponent(applicationContext, token)
            .notificationServiceComponent()
            .build()
    }

    @Inject
    lateinit var viewModel: NotificationViewModel

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    private val job = SupervisorJob()
    private val coroutineScope by lazy { CoroutineScope(dispatchers.background + job) }

    private val notificationManager by lazy { NotificationManagerCompat.from(applicationContext) }

    private val binder: ServiceBinder by lazy { ServiceBinder() }
    private val commandReceiver: CommandReceiver by lazy { CommandReceiver() }

    private var accessToken: AccessToken? = null

    override fun onCreate() {
        super.onCreate()

        val session = runBlocking { observeSession().first() }
        if (session == null) {
            stopSelf()
            return
        }
        accessToken = session.toAccessToken()
        component.inject(this)

        viewModel.consumeEffects(lifecycleScope, this::handleEffect)
        viewModel.consumeStates(lifecycleScope, this::render)
        viewModel.dispatch(NotificationIntent.Initialize)

        registerReceiver(commandReceiver, IntentFilter(ACTION))
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val notification = showNotification()
        startForeground(R.id.tweet_notification, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            unregisterReceiver(commandReceiver)
        } catch (e: Exception) {
            // suppress exception
            // http://stackoverflow.com/questions/12421449/android-broadcastreceiver-unregisterreceiver-issue-not-registered#answer-31276205
            // https://github.com/yshrsmz/omnitweety-android/issues/22
        }

        viewModel.onCleared()
    }

    private fun handleEffect(effect: NotificationEffect) {
        when (effect) {
            NotificationEffect.UpdateCompleted -> {
                closeNotificationDrawer()
                showToast(getString(R.string.tweet_succeeded))
                updateNotification(viewModel.state)
            }
            is NotificationEffect.Error -> {
                closeNotificationDrawer()
                showToast(effect.message, Duration.LONG)
                updateNotification(viewModel.state)
            }
            is NotificationEffect.StatusTooLong -> {
                closeNotificationDrawer()
                showToast(getString(R.string.status_too_long))
                updateNotification(viewModel.state)
                startActivity(ComposeActivity.callingIntent(applicationContext, effect.status))
            }
            NotificationEffect.StopNotification -> stopSelf()
            NotificationEffect.OpenTweetDialog ->
                startActivity(ComposeActivity.callingIntent(applicationContext))
        }
    }

    private fun render(state: NotificationState) {
        if (state.state == ULIEState.UNINITIALIZED || state.state == ULIEState.LOADING) return

        updateNotification(state)
    }

    private fun showNotification(): Notification {
        return updateNotification(viewModel.state)
    }

    private fun updateNotification(state: NotificationState): Notification {
        val notification = buildNotification(state)
        notification.flags = NotificationCompat.FLAG_NO_CLEAR
        notificationManager.notify(R.id.tweet_notification, notification)
        return notification
    }

    private fun buildNotification(state: NotificationState): Notification {
        val directTweetIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            commandIntent(Command.DIRECT_TWEET),
            PendingIntent.FLAG_UPDATE_CURRENT)

        val openDialogIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            ComposeActivity.callingIntent(applicationContext),
            PendingIntent.FLAG_CANCEL_CURRENT)

        val closeIntent = PendingIntent.getBroadcast(
            applicationContext,
            2,
            commandIntent(Command.CLOSE_NOTIFICATION),
            PendingIntent.FLAG_CANCEL_CURRENT)

        val openSettingsIntent = TaskStackBuilder.create(applicationContext)
            .addParentStack(MainActivity::class.java)
            .addNextIntent(MainActivity.callingIntent(applicationContext))
            .getPendingIntent(3, PendingIntent.FLAG_UPDATE_CURRENT)

        val remoteInput = RemoteInput.Builder(KEY_NOTIFICATION_TWEET_TEXT)
            .setLabel(getString(R.string.whats_happening))
            .build()

        val directTweetAction = NotificationCompat.Action.Builder(
            R.drawable.ic_send_black_24dp,
            getString(R.string.tweet).toUpperCase(Locale.ENGLISH),
            directTweetIntent)
            .addRemoteInput(remoteInput)
            .build()

        val closeAction = NotificationCompat.Action.Builder(
            R.drawable.ic_close_black_24dp,
            getString(R.string.close).toUpperCase(Locale.ENGLISH),
            closeIntent)
            .build()

        val settingAction = NotificationCompat.Action.Builder(
            R.drawable.ic_settings_black_24dp,
            getString(R.string.settings).toUpperCase(Locale.ENGLISH),
            openSettingsIntent)
            .build()

        val footerStateString = if (state.footerEnabled) {
            getString(R.string.notification_footer_on, state.footerText)
        } else {
            getString(R.string.notification_footer_off)
        }

        val inboxStyle = NotificationCompat.InboxStyle()
            .addLine(getString(R.string.notification_content))
            .addLine(footerStateString)

        val builder = NotificationCompat.Builder(applicationContext, Channel.EDITOR.id)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_content))
            .setContentIntent(openDialogIntent)
            .setStyle(inboxStyle)
            .setShowWhen(false)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(directTweetAction)
            .addAction(settingAction)

        if (state.timelineAppEnabled && state.timelineApp != null) {
            val appIntent = PendingIntent.getActivity(
                applicationContext,
                1,
                packageManager.getLaunchIntentForPackage(state.timelineApp.packageName.packageName),
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val appAction = NotificationCompat.Action.Builder(
                R.drawable.ic_view_headline_black_24dp,
                getString(R.string.timeline).toUpperCase(Locale.ENGLISH),
                appIntent)
                .build()
            builder.addAction(appAction)
        }

        builder.addAction(closeAction)

        return builder.build()
    }

    private fun showToast(message: String, duration: Duration = Duration.SHORT) {
        coroutineScope.launch(dispatchers.main) {
            Toast.makeText(applicationContext, message, duration.length).show()
        }
    }

    enum class Duration(val length: Int) {
        SHORT(Toast.LENGTH_SHORT), LONG(Toast.LENGTH_LONG)
    }

    inner class ServiceBinder : Binder() {
        val service: NotificationService
            get() = this@NotificationService
    }

    inner class CommandReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (Command.from(intent?.getStringExtra(KEY_COMMAND) ?: "")) {
                Command.CLOSE_NOTIFICATION -> closeNotificationDrawer()
                Command.DIRECT_TWEET -> {
                    val bundle = RemoteInput.getResultsFromIntent(intent) ?: return
                    val status = bundle.getString(KEY_NOTIFICATION_TWEET_TEXT)
                        .takeUnless { it.isNullOrBlank() } ?: return

                    viewModel.dispatch(NotificationIntent.Tweet(status))
                }
                Command.SHOW_TWEET_DIALOG -> {
                    viewModel.dispatch(NotificationIntent.OpenTweetDialog)
                }
            }
        }
    }

    companion object {
        const val KEY_COMMAND = "notification_command"
        const val KEY_NOTIFICATION_TWEET_TEXT = "notification_tweet_text"
        private const val ACTION =
            "net.yslibrary.monotweety.notification.NotificationService.Action"

        fun commandIntent(command: Command): Intent {
            return Intent().apply {
                action = ACTION
                putExtra(KEY_COMMAND, command.value)
            }
        }

        fun callingIntent(context: Context): Intent {
            return Intent(context.applicationContext, NotificationService::class.java)
        }
    }

    enum class Command(val value: String) {
        CLOSE_NOTIFICATION("net.yslibrary.monotweety.notification.NotificationService.CloseNotification"),
        DIRECT_TWEET("net.yslibrary.monotweety.notification.NotificationService.DirectTweet"),
        SHOW_TWEET_DIALOG("net.yslibrary.monotweety.notification.NotificationService.ShowTweetDialog");

        companion object {
            fun from(value: String): Command? {
                return values().firstOrNull { it.value == value }
            }
        }
    }
}

private fun Service.closeNotificationDrawer() {
    applicationContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
}
