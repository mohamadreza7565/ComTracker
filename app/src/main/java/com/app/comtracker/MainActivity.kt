package com.app.comtracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app.comtracker.ui.theme.ComTrackerTheme
import com.app.comtracker.core.services.RetryForegroundService
import com.app.comtracker.feature.home.HomeScreen
import com.app.comtracker.utilities.extensions.PermissionExt

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        PermissionExt.checkAndRequestPermissions(
            activity = this,
            onFailed = {}
        )

        setContent {
            ComTrackerTheme {
                HomeScreen()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        startService()
    }

    private fun startService() {
        val intent = Intent(this, RetryForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }


}



