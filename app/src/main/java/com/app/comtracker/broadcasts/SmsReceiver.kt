package com.app.comtracker.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.app.comtracker.utilities.PrefExt

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
                Toast.makeText(
                    context,
                    "ComTrackerLogChecker SmsReceiver -> read \n ($phoneNumber)\n($message)",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i(
                    "TAG", "ComTrackerLogChecker SmsReceiver -> read \n ($phoneNumber)\n($message)"
                )

//                saveOrSendSms(context, phoneNumber, message)
            }
        }
    }
}
