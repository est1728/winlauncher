package com.example.winlauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.winlauncher.ui.theme.WallpaperOption

/**
 * วาดวอลเปเปอร์แบบ abstract: พื้นสีเบส + วง gradient นุ่มๆ 3 วงซ้อนกัน
 * ให้ความรู้สึกคล้ายภาพ "bloom" ของ Windows 11 แต่เป็นภาพที่สร้างขึ้นเอง ไม่ใช่ของจริง
 */
@Composable
fun WallpaperBackground(option: WallpaperOption, modifier: Modifier = Modifier) {
    val blobs = option.blobColors
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .fillMaxSize()
            .background(option.baseColor)
            .background(
                Brush.radialGradient(
                    colors = listOf(blobs.getOrElse(0) { option.baseColor }.copy(alpha = 0.85f), androidx.compose.ui.graphics.Color.Transparent),
                    radius = 1400f,
                )
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(blobs.getOrElse(1) { option.baseColor }.copy(alpha = 0.75f), androidx.compose.ui.graphics.Color.Transparent),
                    radius = 1600f,
                )
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(blobs.getOrElse(2) { option.baseColor }.copy(alpha = 0.6f), androidx.compose.ui.graphics.Color.Transparent),
                    radius = 1000f,
                )
            )
    )
}
