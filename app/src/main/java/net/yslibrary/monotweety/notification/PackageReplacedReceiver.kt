package net.yslibrary.monotweety.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import net.yslibrary.monotweety.App
import rx.Observable
import timber.log.Timber

class PackageReplacedReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    Timber.d("package replaced")

    Observable.zip(
        App.appComponent(context).notificationEnabledManager().get(),
        App.appComponent(context).isLoggedIn().execute().toObservable(),
        { enabled, loggedIn -> enabled && loggedIn })
        .first()
        .subscribe {
          Timber.d("is logged in and notification enabled: $it")
          if (it) {
            ContextCompat.startForegroundService(context, NotificationService.callingIntent(context))
          }
        }
  }
}