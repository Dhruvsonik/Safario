package com.example.safario

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.safario.navigation.NavGraph
import com.example.safario.utils.SessionManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val context = this
            val session = SessionManager(context)

            var isDarkMode by remember {
                mutableStateOf(session.isDarkMode())
            }

            MaterialTheme(
                colorScheme = if (isDarkMode)
                    darkColorScheme()
                else
                    lightColorScheme()
            ) {

                // 🔥 IMPORTANT: NavGraph INSIDE theme
                NavGraph(
                    isDarkMode = isDarkMode,
                    onThemeChange = {
                        isDarkMode = it
                        session.saveTheme(it)
                    }
                )
            }
        }

        requestPermissions() // move here
    }

    private fun requestPermissions() {

        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.SEND_SMS
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                100
            )
        }
    }
}