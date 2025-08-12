package com.app.comtracker.core.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.app.comtracker.core.di.UseCaseEntryPoint
import com.app.comtracker.core.network.api
import com.app.comtracker.utilities.extensions.NotificationExt
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RetryForegroundService : Service() {


    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            Log.i("TAG", "ComTrackerLogChecker Foreground Service -> retry")

            val entryPoint = EntryPointAccessors.fromApplication(
                applicationContext,
                UseCaseEntryPoint::class.java
            )
            val postHistoriesTrackUseCase = entryPoint.postHistoriesTrackUseCase()
            val job = Job()
            val scope = CoroutineScope(Dispatchers.IO + job)

            scope.launch {
                api(
                    scope = this,
                    block = { postHistoriesTrackUseCase() },
                    callBack = {
                        onSuccess {
                            Log.i("TAG", "ComTrackerLogChecker Foreground Service -> onSuccess")
                            postDelayed()
                            job.cancel()
                        }
                        onError { _, _ ->
                            Log.i("TAG", "ComTrackerLogChecker Foreground Service -> onError")
                            postDelayed()
                            job.cancel()
                        }
                    }
                )
            }


        }
    }

    private fun postDelayed() {
        handler.postDelayed(runnable, 10 * 60_000)
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
