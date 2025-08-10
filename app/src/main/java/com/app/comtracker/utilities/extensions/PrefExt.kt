package com.app.comtracker.utilities.extensions

import android.content.Context
import androidx.core.content.edit

object PrefExt {

    const val PREFS_NAME = "com_tracker_prefs"
    const val KEY_TRACK_CALLS = "track_calls"
    const val KEY_TRACK_SMS = "track_sms"

    fun isSmsTrackingEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_TRACK_SMS, true)
    }

    fun updateSmsTracking(value: Boolean, context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_TRACK_SMS, value) }
    }

    fun isCallTrackingEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_TRACK_CALLS, true)
    }

    fun updateCallTracking(value: Boolean, context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_TRACK_CALLS, value) }
    }
}