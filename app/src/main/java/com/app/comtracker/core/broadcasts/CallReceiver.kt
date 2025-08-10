package com.app.comtracker.core.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.app.comtracker.core.di.UseCaseEntryPoint
import com.app.comtracker.core.network.api
import com.app.comtracker.utilities.extensions.PrefExt
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (!PrefExt.isCallTrackingEnabled(context = context)) return

        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            Log.i("TAG", "ComTrackerLogChecker CallReceiver -> received")
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                incomingNumber?.let {
                    Log.i("TAG", "ComTrackerLogChecker CallReceiver -> read ($it)")

                    // TODO Save to db with error flag

                    val entryPoint = EntryPointAccessors.fromApplication(
                        context,
                        UseCaseEntryPoint::class.java
                    )
                    val postSingleTrackUseCases = entryPoint.postSingleTrackUseCases()

                    val job = Job()
                    val scope = CoroutineScope(Dispatchers.IO + job)

                    api(
                        scope = scope,
                        block = { postSingleTrackUseCases() },
                        callBack = {
                            onSuccess {
                                Log.i("TAG", "ComTrackerLogChecker CallReceiver -> onSuccess")
                                job.cancel() // Release resource
                            }
                            onError { _, _ ->
                                Log.i("TAG", "ComTrackerLogChecker CallReceiver -> onError")
                                // TODO Update db with success flag
                                job.cancel() // Release resource
                            }
                        }
                    )

                }
            }
        }
    }
}