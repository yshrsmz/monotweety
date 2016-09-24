package net.yslibrary.monologue.activity.launcher

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.RemoteInput
import android.support.v4.app.TaskStackBuilder
import android.widget.Button
import net.yslibrary.monologue.R
import net.yslibrary.monologue.base.BaseActivity
import org.jetbrains.anko.find

/**
 * Created by yshrsmz on 2016/09/24.
 */
class LauncherActivity : BaseActivity() {

  val KEY_TEXT = "key_text"

  val showNotiBtn by lazy { find<Button>(R.id.show_notification) }
  val notificationManager: NotificationManagerCompat
      by lazy { NotificationManagerCompat.from(applicationContext) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_launcher)

    showNotiBtn.setOnClickListener {
      val label = "tweet"
      val intent = Intent(applicationContext, LauncherActivity::class.java)
      val pendingIntent = TaskStackBuilder.create(this)
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
  }
}