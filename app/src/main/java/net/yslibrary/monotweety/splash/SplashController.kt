package net.yslibrary.monotweety.splash

import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.RemoteInput
import android.support.v4.app.TaskStackBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.activity.launcher.LauncherActivity
import net.yslibrary.monotweety.base.BaseController
import net.yslibrary.monotweety.base.findById
import kotlin.properties.Delegates

/**
 * Created by yshrsmz on 2016/09/25.
 */
class SplashController : BaseController() {

  val KEY_TEXT = "key_text"

  var showNotiBtn by Delegates.notNull<Button>()

  val notificationManager: NotificationManagerCompat
      by lazy { NotificationManagerCompat.from(applicationContext) }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_splash, container, false)

    showNotiBtn = view.findById(R.id.show_notification)
    showNotiBtn.setOnClickListener {
      val label = "tweet"
      val intent = Intent(applicationContext, LauncherActivity::class.java)
      val pendingIntent = TaskStackBuilder.create(activity)
          .addParentStack(LauncherActivity::class.java)
          .addNextIntent(intent)
          .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)


      val remoteInput = RemoteInput.Builder(KEY_TEXT)
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

      notificationManager.notify(0, builder.build())
    }

    return view
  }
}