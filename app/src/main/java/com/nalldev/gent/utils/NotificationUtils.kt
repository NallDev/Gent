package com.nalldev.gent.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.nalldev.gent.R
import com.nalldev.gent.domain.models.ListEventsItem

object NotificationUtils {

    private const val CHANNEL_ID = "event_notification_channel"
    private const val CHANNEL_NAME = "Event Notifications"
    private const val NOTIFICATION_ID = 1

    fun showEventNotification(context: Context, event: ListEventsItem) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for event notifications"
        }
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_upcoming)
            .setContentTitle(event.name)
            .setContentText("Event starts at ${event.beginTime?.let {
                DateHelper.changeFormat("yyyy-MM-dd HH:mm:ss", "dd MMM yyyy",
                    it
                )
            }}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}