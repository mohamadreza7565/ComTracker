package com.app.comtracker.core.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
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

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (!PrefExt.isSmsTrackingEnabled(context)) return

        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            val pdus = bundle?.get("pdus") as? Array<*>

            Log.i("TAG", "ComTrackerLogChecker SmsReceiver -> received")

            if (pdus != null && pdus.isNotEmpty()) {
                val format = bundle.getString("format")
                val fullMessage = StringBuilder()
                var phoneNumber: String? = null

                for (pdu in pdus) {
                    val sms = SmsMessage.createFromPdu(pdu as ByteArray, format)
                    if (phoneNumber == null) {
                        phoneNumber = sms.originatingAddress
                    }
                    fullMessage.append(sms.messageBody)
                }

                Log.i("TAG", "ComTrackerLogChecker SmsReceiver -> $fullMessage - $phoneNumber")

                phoneNumber?.let { number ->
                    val job = Job()
                    val scope = CoroutineScope(Dispatchers.IO + job)

                    val entryPoint = EntryPointAccessors.fromApplication(
                        context,
                        UseCaseEntryPoint::class.java
                    )
                    val postSingleTrackUseCases = entryPoint.postSingleTrackUseCase()
                    val addHistoryUseCase = entryPoint.addHistoryUseCase()

                    var trackerId: Long = 0
                    scope.launch {
                        trackerId = async { addHistoryUseCase(
                            type = TrackerHistoryType.SMS,
                            phoneNumber = number,
                            message = fullMessage.toString()
                        ) }.await()

                        api(
                            scope = this,
                            block = { postSingleTrackUseCases(trackerId) },
                            callBack = {
                                onSuccess {
                                    Log.i("TAG", "ComTrackerLogChecker SmsReceiver -> onSuccess")
                                job.cancel()
                                }
                                onError { _, _ ->
                                    Log.i("TAG", "ComTrackerLogChecker SmsReceiver -> onError")
                                job.cancel()
                                }
                            }
                        )
                    }


                }
            }
        }
    }
}
