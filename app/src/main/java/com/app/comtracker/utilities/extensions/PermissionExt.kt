package com.app.comtracker.utilities.extensions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

object PermissionExt {

    private val permissions = mutableListOf(
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.INTERNET
    ).also {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            it.add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }


    fun checkAndRequestPermissions(activity: ComponentActivity, onFailed: () -> Unit) {
        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty()) {

            val requestPermissionsLauncher =
                activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    val allGranted = permissions.all { it.value }
                    if (!allGranted) {
                        onFailed()
                    }
                }

            requestPermissionsLauncher.launch(missingPermissions.toTypedArray())
        }
    }

}