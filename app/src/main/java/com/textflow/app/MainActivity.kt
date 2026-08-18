package com.textflow.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.textflow.app.ui.screens.HomeScreen
import com.textflow.app.ui.theme.TextFlowTheme

/** Entry point. For now it hosts the dev home screen; later it becomes the
 *  onboarding / sign-in entry that also drives the overlay + accessibility
 *  permission flows. */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextFlowTheme {
                HomeScreen(
                    overlayGranted = OverlayPermission.isGranted(this),
                    onRequestOverlay = {
                        startActivity(OverlayPermission.settingsIntent(this))
                    },
                )
            }
        }
    }
}

/** SYSTEM_ALERT_WINDOW helpers: the overlay permission intent (spec §1 / §4). */
object OverlayPermission {

    fun isGranted(context: Context): Boolean = Settings.canDrawOverlays(context)

    /** Intent that opens the system "Display over other apps" page for our app. */
    fun settingsIntent(context: Context): Intent =
        Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}"),
        )
}
