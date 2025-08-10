package com.app.comtracker.core.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.app.comtracker.core.di.UseCaseEntryPoint
import com.app.comtracker.core.network.api
import com.app.comtracker.utilities.extensions.PrefExt
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (!PrefExt.isSmsTrackingEnabled(context = context)) return

        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            val pdus = bundle?.get("pdus") as? Array<*>

            Log.i("TAG", "ComTrackerLogChecker SmsReceiver -> received")

            pdus?.forEach { pdu ->
                val format = bundle.getString("format")
                val sms = SmsMessage.createFromPdu(pdu as ByteArray, format)
                val phoneNumber = sms.originatingAddress ?: ""
                val message = sms.messageBody

                // TODO Save to db with error flag

                Log.i(
                    "TAG", "ComTrackerLogChecker SmsReceiver -> read \n ($phoneNumber)\n($message)"
                )

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
                            Log.i("TAG", "ComTrackerLogChecker SmsReceiver -> onSuccess")
                            // TODO Update db with success flag
                            job.cancel() // Release resource
                        }
                        onError { _, _ ->
                            Log.i("TAG", "ComTrackerLogChecker SmsReceiver -> onError")
                            job.cancel() // Release resource
                        }
                    }
                )
            }
        }
    }
}
