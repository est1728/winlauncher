package com.example.winlauncher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Win11Blue = Color(0xFF0078D4)
val Win11Desktop = Color(0xFF1E3A5F)
val Win11TaskbarDark = Color(0xE6202020)
val Win11TaskbarLight = Color(0xE6FBFBFB)
val Win11WindowTitleLight = Color(0xFFF3F3F3)
val Win11WindowTitleDark = Color(0xFF2B2B2B)
val Win11WindowBodyLight = Color.White
val Win11WindowBodyDark = Color(0xFF202020)
val Win11TextLight = Color(0xFF1B1B1B)
val Win11TextDark = Color(0xFFF3F3F3)
val Win11CloseRed = Color(0xFFE81123)

// สีพื้น "ไทล์" ไอคอนแอปบนเดสก์ท็อป ให้ไอคอนเด่นชัดบนวอลเปเปอร์ทุกแบบ
val AppTileFileExplorer = Color(0xFFFFB900)
val AppTileSettings = Color(0xFF5A5A5A)
val AppTileNotepad = Color(0xFF3A8DFF)
val AppTileCalculator = Color(0xFF2B2B2B)

data class DesktopColors(
    val taskbar: Color,
    val windowTitleBar: Color,
    val windowBody: Color,
    val text: Color,
    val taskbarText: Color,
)

fun desktopColorsFor(isDark: Boolean): DesktopColors = if (isDark) {
    DesktopColors(
        taskbar = Win11TaskbarDark,
        windowTitleBar = Win11WindowTitleDark,
        windowBody = Win11WindowBodyDark,
        text = Win11TextDark,
        taskbarText = Color.White,
    )
} else {
    DesktopColors(
        taskbar = Win11TaskbarLight,
        windowTitleBar = Win11WindowTitleLight,
        windowBody = Win11WindowBodyLight,
        text = Win11TextLight,
        taskbarText = Color.Black,
    )
}

private val LightColors = lightColorScheme(
    primary = Win11Blue,
    background = Color(0xFFF3F3F3),
    surface = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Win11Blue,
    background = Color(0xFF202020),
    surface = Win11WindowTitleDark,
)

@Composable
fun WinLauncherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
