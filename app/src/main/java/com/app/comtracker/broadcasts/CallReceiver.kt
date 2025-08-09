package com.app.comtracker.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            Log.i("TAG", "ComTrackerLogChecker CallReceiver -> received")
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                incomingNumber?.let {
                    Log.i("TAG", "ComTrackerLogChecker CallReceiver -> read ($it)")
                    // اینجا شماره رو ذخیره یا ارسال کن
                    // saveOrSendCall(context, it)
                }
            }
        }
    }
}