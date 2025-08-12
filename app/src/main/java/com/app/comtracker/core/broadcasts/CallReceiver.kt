package com.app.comtracker.core.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.app.comtracker.core.di.UseCaseEntryPoint
import com.app.comtracker.core.network.api
import com.app.comtracker.domain.model.TrackerHistoryType
import com.app.comtracker.utilities.extensions.PrefExt
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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

                    val job = Job()
                    val scope = CoroutineScope(Dispatchers.IO + job)

                    val entryPoint = EntryPointAccessors.fromApplication(
                        context, UseCaseEntryPoint::class.java
                    )
                    val postSingleTrackUseCases = entryPoint.postSingleTrackUseCase()
                    val addHistoryUseCase = entryPoint.addHistoryUseCase()
                    var trackerId: Long = 0
                    scope.launch {
                        trackerId = async {
                            addHistoryUseCase(
                                type = TrackerHistoryType.CALL, phoneNumber = it
                            )
                        }.await()
                        api(
                            scope = scope,
                            block = { postSingleTrackUseCases(trackerId) },
                            callBack = {
                                onSuccess {
                                    Log.i("TAG", "ComTrackerLogChecker CallReceiver -> onSuccess")
                                    job.cancel()
                                }
                                onError { _, _ ->
                                    Log.i("TAG", "ComTrackerLogChecker CallReceiver -> onError")
                                    job.cancel()
                                }
                            })
                    }


                }
            }
        }
    }
}