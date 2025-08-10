package com.app.comtracker.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ComTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
