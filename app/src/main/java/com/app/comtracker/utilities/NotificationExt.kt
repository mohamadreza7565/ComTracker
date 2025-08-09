package com.app.comtracker.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.comtracker.services.RetryForegroundService

object NotificationExt {

    private const val NOTIFICATION_ID = 1
    private const val NOTIFICATION_CHANNEL_ID = "retry_service_channel"
    private const val NOTIFICATION_CHANNEL_NAME = "Retry Service"
    private const val NOTIFICATION_TITLE = "Service is up"
    private const val NOTIFICATION_CONTENT = "The service is active and tracking data"

    fun startForegroundServiceNotification(service: RetryForegroundService) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            service.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }


        val notification = NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_CONTENT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(getMessage(service)))
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .build()

        service.startForeground(NOTIFICATION_ID, notification)
    }

    private fun getMessage(context: Context): String {
        val isTrackingSms = PrefExt.isSmsTrackingEnabled(context = context)
        val isTrackingCall = PrefExt.isCallTrackingEnabled(context = context)
        var message = "The service is active and tracking data\n\n"

        message += if (isTrackingCall) {
            "Call control is active ✅\n"
        } else {
            "Call control is not enabled ❌\n"
        }

        message += if (isTrackingSms) {
            "Sms control is active ✅"
        } else {
            "Sms control is not enabled ❌"
        }

        return message
    }

    fun cancelNotification(context: Context) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }


    fun updateNotification(context: Context) {

        val notification =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_CONTENT)
                .setStyle(NotificationCompat.BigTextStyle().bigText(getMessage(context)))
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .build()

        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}