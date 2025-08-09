package com.app.comtracker.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.app.comtracker.utilities.NotificationExt

class RetryForegroundService : Service() {


    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            Log.i("TAG", "ComTrackerLogChecker Foreground Service -> retry")
            handler.postDelayed(this, 10_000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        NotificationExt.startForegroundServiceNotification(this@RetryForegroundService)
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        NotificationExt.cancelNotification(this)
        handler.removeCallbacks(runnable)
    }

    override fun onBind(intent: Intent?): IBinder? = null

}
