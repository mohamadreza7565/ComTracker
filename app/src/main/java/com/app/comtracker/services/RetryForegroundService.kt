package com.app.comtracker.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import android.util.Log

class RetryForegroundService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            Log.i("TAG", "ComTrackerLogChecker Foreground Service -> retry")
            // اینجا لاجیک retry خودت رو میذاری
            handler.postDelayed(this, 10_000) // هر 10 ثانیه
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundServiceNotification()
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundServiceNotification() {
        val channelId = "retry_service_channel"
        val channelName = "Retry Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("در حال بررسی داده‌ها")
            .setContentText("سرویس فعال است و داده‌ها را ارسال می‌کند")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .build()

        startForeground(1, notification)
    }
}
