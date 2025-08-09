package com.app.comtracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.app.comtracker.ui.theme.ComTrackerTheme
import androidx.core.content.edit
import com.app.comtracker.services.RetryForegroundService

class MainActivity : ComponentActivity() {

    companion object {
        const val PREFS_NAME = "com_tracker_prefs"
        const val KEY_TRACK_CALLS = "track_calls"
        const val KEY_TRACK_SMS = "track_sms"
    }

    private lateinit var prefs: SharedPreferences

    private val permissions = arrayOf(
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.INTERNET
    )

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // میتونی اینجا وضعیت دسترسی‌ها رو بررسی کنی اگر خواستی

            val allGranted = permissions.all { it.value } // همه true باشه

            if (!allGranted) {
                // حتی یک دسترسی هم رد شده → اپ رو ببند
//                finishAffinity() // بستن همه اکتیویتی‌ها
//                android.os.Process.killProcess(android.os.Process.myPid()) // کشتن پروسه
                Log.i("TAG", "permissions -> not granted")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // درخواست دسترسی‌ها در شروع اپ
        checkAndRequestPermissions()

        setContent {
            ComTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty()) {
            requestPermissionsLauncher.launch(missingPermissions.toTypedArray())
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, RetryForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }


    @Composable
    fun Greeting(
        modifier: Modifier = Modifier
    ) {

        var trackCalls by remember {
            mutableStateOf(prefs.getBoolean(KEY_TRACK_CALLS, true))
        }
        var trackSms by remember {
            mutableStateOf(prefs.getBoolean(KEY_TRACK_SMS, true))
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
                        prefs.edit { putBoolean(KEY_TRACK_CALLS, it) }
                    }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Track Incoming SMS", modifier = Modifier.weight(1f))
                Switch(
                    checked = trackSms,
                    onCheckedChange = {
                        trackSms = it
                        prefs.edit { putBoolean(KEY_TRACK_SMS, it) }
                    }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ComTrackerTheme {
            Greeting()
        }
    }

}



