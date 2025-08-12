package com.app.comtracker.core.network

import android.util.Log
import io.ktor.client.plugins.logging.Logger
fun log() = object : Logger {
    override fun log(message: String) {
        Log.d("ComTracker HTTP call", message)
    }
}
