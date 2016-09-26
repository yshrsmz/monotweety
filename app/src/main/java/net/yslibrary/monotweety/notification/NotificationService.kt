package net.yslibrary.monotweety.notification

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.support.annotation.StringDef
import android.widget.Toast
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.HasComponent
import timber.log.Timber

class NotificationService : Service(), NotificationServiceContract.View, HasComponent<NotificationServiceComponent> {

  companion object {
    const val ACTION = "net.yslibrary.monotweety.notification.NotificationService.Action"

    const val KEY_COMMAND = "notification_command"

    const val COMMAND_SHOW_NOTIFICATION = "net.yslibrary.monotweety.notification.NotificationService.ShowNotification"
    const val COMMAND_HIDE_NOTIFICATION = "net.yslibrary.monotweety.notification.NotificationService.HideNotification"
    const val COMMAND_DIRECT_TWEET = "net.yslibrary.monotweety.notification.NotificationService.DirectTweet"
    const val COMMAND_SHOW_TWEET_DIALOG = "net.yslibrary.monotweety.notification.NotificationService.ShowTweetDialog"

    fun cammandIntent(@CommandType command: String): Intent {
      val intent = Intent()
      intent.action = ACTION
      intent.putExtra(KEY_COMMAND, command)

      return intent
    }
  }

  val binder: IBinder by lazy { ServiceBinder() }
  val commandReceiver: NotificationCommandReceiver by lazy { NotificationCommandReceiver() }

  override val component: NotificationServiceComponent by lazy {
    DaggerNotificationServiceComponent.builder()
        .userComponent(App.userComponent(this))
        .build()
  }

  override fun onCreate() {
    super.onCreate()
    Timber.d("onCreate")

    // check login status first

    injectDependencies()

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

  override fun showNotification() {
    Timber.d("show notification")
  }

  override fun hideNotification() {
    Timber.d("hide notification")
  }

  override fun showTweetDialog(text: String) {

  }

  override fun showTweetSucceeded() {
    Toast.makeText(applicationContext, getString(R.string.message_tweet_succeeded), Toast.LENGTH_SHORT)
        .show()
  }

  override fun showTweetFailed() {
    Toast.makeText(applicationContext, getString(R.string.error_tweet_failed), Toast.LENGTH_SHORT)
        .show()
  }

  override fun showTweetFailedBecauseOfLength() {
    Toast.makeText(applicationContext, getString(R.string.error_tweet_failed_because_of_length), Toast.LENGTH_SHORT)
        .show()
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

      command?.let {
        when (it) {
          COMMAND_SHOW_NOTIFICATION -> {
          }
          COMMAND_HIDE_NOTIFICATION -> {
          }
          COMMAND_DIRECT_TWEET -> {
          }
          COMMAND_SHOW_TWEET_DIALOG -> {
          }
        }
      }
    }
  }
}
