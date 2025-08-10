package com.app.comtracker.feature.home

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.comtracker.utilities.extensions.NotificationExt
import com.app.comtracker.utilities.extensions.PrefExt

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    var trackCalls by remember { mutableStateOf(PrefExt.isCallTrackingEnabled(context)) }
    var trackSms by remember { mutableStateOf(PrefExt.isSmsTrackingEnabled(context)) }

    LaunchedEffect(key1 = trackSms, key2 = trackCalls) {
        NotificationExt.updateNotification(context)
    }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
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
                        PrefExt.updateCallTracking(it, context)
                    }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Track Incoming SMS", modifier = Modifier.weight(1f))
                Switch(
                    checked = trackSms,
                    onCheckedChange = {
                        trackSms = it
                        PrefExt.updateSmsTracking(it, context)
                    }
                )
            }
        }
    }

}
