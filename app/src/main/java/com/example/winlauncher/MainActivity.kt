package com.example.winlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.winlauncher.ui.DesktopScreen
import com.example.winlauncher.ui.theme.WinLauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // ให้เนื้อหาเต็มจอเหมือน desktop OS จริง ไม่มีแถบขอบระบบมาบัง
        setContent {
            WinLauncherTheme {
                DesktopScreen()
            }
        }
    }
}
