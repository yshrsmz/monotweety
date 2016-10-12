package net.yslibrary.monotweety.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import net.yslibrary.monotweety.App
import rx.Observable
import timber.log.Timber

/**
 * Created by yshrsmz on 2016/10/12.
 */
class BootCompletedReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    Timber.d("boot completed ---")

    Observable.zip(
        App.appComponent(context).notificationEnabledManager().get(),
        App.appComponent(context).isLoggedIn().execute().toObservable(),
        { enabled, loggedIn -> enabled && loggedIn })
        .first()
        .subscribe {
          Timber.d("is logged in and notification enabled: $it")
          if (it) {
            context.startService(NotificationService.callingIntent(context))
          }
        }
  }
}