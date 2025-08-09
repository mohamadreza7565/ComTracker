package com.app.comtracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.comtracker.ui.theme.ComTrackerTheme
import com.app.comtracker.services.RetryForegroundService
import com.app.comtracker.utilities.NotificationExt
import com.app.comtracker.utilities.PermissionExt
import com.app.comtracker.utilities.PrefExt

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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
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


    @Composable
    fun HomeScreen(
        modifier: Modifier = Modifier
    ) {

        var trackCalls by remember { mutableStateOf(PrefExt.isCallTrackingEnabled(this)) }
        var trackSms by remember { mutableStateOf(PrefExt.isSmsTrackingEnabled(this)) }

        LaunchedEffect(key1 = trackSms, key2 = trackCalls) {
            NotificationExt.updateNotification(this@MainActivity)
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "ComTracker Settings", style = MaterialTheme.typography.headlineMedium)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Track Incoming Calls", modifier = Modifier.weight(1f))
                Switch(
                    checked = trackCalls,
                    onCheckedChange = {
                        trackCalls = it
                        PrefExt.updateCallTracking(it, this@MainActivity)
                    }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Track Incoming SMS", modifier = Modifier.weight(1f))
                Switch(
                    checked = trackSms,
                    onCheckedChange = {
                        trackSms = it
                        PrefExt.updateSmsTracking(it, this@MainActivity)
                    }
                )
            }
        }
    }


}



