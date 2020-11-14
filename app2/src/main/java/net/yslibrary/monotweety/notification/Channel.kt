package net.yslibrary.monotweety.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import net.yslibrary.monotweety.R

@TargetApi(Build.VERSION_CODES.O)
enum class Channel(
    val id: String,
    val nameResId: Int,
    val descriptionResId: Int,
    val importance: Int,
    val lockScreenVisibility: Int,
    val showBadge: Boolean,
) {
    EDITOR(
        id = "editor",
        nameResId = R.string.notification_channel_editor_name,
        descriptionResId = R.string.notification_channel_editor_description,
        importance = NotificationManager.IMPORTANCE_LOW,
        lockScreenVisibility = Notification.VISIBILITY_PUBLIC,
        showBadge = false
    )
}

fun createNotificationChannel(context: Context, notificationManager: NotificationManager) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }

    val channels = Channel.values().map { channelInfo ->
        NotificationChannel(
            channelInfo.id,
            context.getString(channelInfo.nameResId),
            channelInfo.importance
        )
            .apply {
                description = context.getString(channelInfo.descriptionResId)
                enableLights(false)
                enableVibration(false)
                setShowBadge(channelInfo.showBadge)
                lockscreenVisibility = channelInfo.lockScreenVisibility
            }
    }
    notificationManager.createNotificationChannels(channels)
}
