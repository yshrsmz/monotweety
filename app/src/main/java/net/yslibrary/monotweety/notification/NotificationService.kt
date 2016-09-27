package net.yslibrary.monotweety.notification

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.support.annotation.StringDef
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.RemoteInput
import android.widget.Toast
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.HasComponent
import rx.Completable
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

class NotificationService : Service(), HasComponent<NotificationComponent> {

  companion object {
    const val KEY_NOTIFICATION_TWEET_TEXT = "notification_tweet_text"

    const val ACTION = "net.yslibrary.monotweety.notification.NotificationService.Action"

    const val KEY_COMMAND = "notification_command"

    const val COMMAND_SHOW_NOTIFICATION = "net.yslibrary.monotweety.notification.NotificationService.ShowNotification"
    const val COMMAND_HIDE_NOTIFICATION = "net.yslibrary.monotweety.notification.NotificationService.HideNotification"
    const val COMMAND_DIRECT_TWEET = "net.yslibrary.monotweety.notification.NotificationService.DirectTweet"
    const val COMMAND_SHOW_TWEET_DIALOG = "net.yslibrary.monotweety.notification.NotificationService.ShowTweetDialog"

    fun commandIntent(@CommandType command: String): Intent {
      val intent = Intent()
      intent.action = ACTION
      intent.putExtra(KEY_COMMAND, command)

      return intent
    }
  }

  private val binder: IBinder by lazy { ServiceBinder() }
  private val commandReceiver: NotificationCommandReceiver by lazy { NotificationCommandReceiver() }

  @field:[Inject]
  lateinit var viewModel: NotificationServiceViewModel

  @field:[Inject]
  lateinit var notificationManager: NotificationManagerCompat

  private val subscriptions = CompositeSubscription()


  override val component: NotificationComponent by lazy {
    DaggerNotificationComponent.builder()
        .userComponent(App.userComponent(this))
        .notificationServiceModule(NotificationServiceModule(this))
        .build()
  }

  override fun onCreate() {
    super.onCreate()
    Timber.d("onCreate")

    // TODO: check login status first

    injectDependencies()
    setEvents()
    registerCommandReceiver()
  }

  override fun onBind(intent: Intent): IBinder? {
    return binder
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Timber.d("onStartCommand: ${intent?.toString()}")

    return START_STICKY
  }

  override fun onDestroy() {
    super.onDestroy()
    Timber.d("onDestroy")
  }

  override fun onUnbind(intent: Intent?): Boolean {
    return super.onUnbind(intent)
  }

  override fun onRebind(intent: Intent?) {
    super.onRebind(intent)
  }

  private fun injectDependencies() {
    component.inject(this)
  }

  private fun registerCommandReceiver() {
    val intentFilter = IntentFilter()
    intentFilter.addAction(ACTION)
    registerReceiver(commandReceiver, intentFilter)
  }

  fun setEvents() {
    viewModel.updateCompleted
        .subscribe {
          updateNotification()
          closeNotificationDrawer()
          showTweetSucceeded()
        }
  }

  fun showNotification() {
    Timber.d("show notification")
    val label = "tweet"
    val intent = commandIntent(COMMAND_DIRECT_TWEET)
    val pendingIntent = PendingIntent
        .getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


    val remoteInput = RemoteInput.Builder(KEY_NOTIFICATION_TWEET_TEXT)
        .setLabel(label).build()

    val notificationAction = NotificationCompat.Action.Builder(
        R.drawable.ic_send_black_24dp,
        "tweet_label",
        pendingIntent)
        .addRemoteInput(remoteInput)
        .build()

    val builder = NotificationCompat.Builder(applicationContext)
        .setSmallIcon(R.drawable.ic_chat_bubble_outline_black_24dp)
        .setContentTitle("Notification Title")
        .setContentText("Notification Text")
        .addAction(notificationAction)

    notificationManager.notify(R.id.tweet_notification, builder.build())
  }

  fun updateNotification() {
    Timber.d("update notification")
    val label = "tweet"
    val intent = commandIntent(COMMAND_DIRECT_TWEET)
    val pendingIntent = PendingIntent
        .getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


    val remoteInput = RemoteInput.Builder(KEY_NOTIFICATION_TWEET_TEXT)
        .setLabel(label).build()

    val notificationAction = NotificationCompat.Action.Builder(
        R.drawable.ic_send_black_24dp,
        "tweet_label",
        pendingIntent)
        .addRemoteInput(remoteInput)
        .build()

    val builder = NotificationCompat.Builder(applicationContext)
        .setSmallIcon(R.drawable.ic_chat_bubble_outline_black_24dp)
        .setContentTitle("Notification Title")
        .setContentText("Notification Text")
        .addAction(notificationAction)

    notificationManager.notify(R.id.tweet_notification, builder.build())
  }

  fun closeNotificationDrawer() {
    val intent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    applicationContext.sendBroadcast(intent)
  }

  fun hideNotification() {
    Timber.d("hide notification")
  }

  fun showTweetDialog(text: String) {

  }

  fun showTweetSucceeded() {
    Completable.fromAction {
      Toast.makeText(applicationContext, getString(R.string.message_tweet_succeeded), Toast.LENGTH_SHORT)
          .show()
    }.subscribeOn(AndroidSchedulers.mainThread())
        .subscribe()
  }

  fun showTweetFailed() {
    Completable.fromAction {
      Toast.makeText(applicationContext, getString(R.string.error_tweet_failed), Toast.LENGTH_SHORT)
          .show()
    }.subscribeOn(AndroidSchedulers.mainThread())
        .subscribe()

  }

  fun showTweetFailedBecauseOfLength() {
    Completable.fromAction {
      Toast.makeText(applicationContext, getString(R.string.error_tweet_failed_because_of_length), Toast.LENGTH_SHORT)
          .show()
    }.subscribeOn(AndroidSchedulers.mainThread())
        .subscribe()
  }

  inner class ServiceBinder : Binder() {
    val service: NotificationService
      get() = this@NotificationService
  }

  @Retention(AnnotationRetention.SOURCE)
  @StringDef(
      COMMAND_SHOW_NOTIFICATION,
      COMMAND_HIDE_NOTIFICATION,
      COMMAND_DIRECT_TWEET,
      COMMAND_SHOW_TWEET_DIALOG)
  annotation class CommandType

  inner class NotificationCommandReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
      val command = intent?.getStringExtra(KEY_COMMAND)

      Timber.d("onReceive - command: $command")

      // TODO: check login status first

      command?.let {
        when (it) {
          COMMAND_SHOW_NOTIFICATION -> viewModel.onShowNotificationCommand()
          COMMAND_HIDE_NOTIFICATION -> viewModel.onHideNotificationCommand()
          COMMAND_DIRECT_TWEET -> {
            val bundle = RemoteInput.getResultsFromIntent(intent)
            bundle?.getString(KEY_NOTIFICATION_TWEET_TEXT)?.let { text ->
              viewModel.onDirectTweetCommand(text)
            }
          }
          COMMAND_SHOW_TWEET_DIALOG -> viewModel.onShowTweetDialogCommand()
          else -> {
            // no-op
          }
        }
      }
    }
  }
}
