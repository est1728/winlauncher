package com.example.winlauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.winlauncher.ui.theme.DesktopColors
import com.example.winlauncher.ui.theme.Win11Blue

@Composable
fun Taskbar(
    manager: DesktopWindowManager,
    colors: DesktopColors,
    onStartClick: () -> Unit,
) {
    val topZ = manager.windows.filter { !it.isMinimized }.maxOfOrNull { it.zIndex }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(colors.taskbar),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Win11Blue)
                    .clickable { onStartClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Apps, contentDescription = "Start", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            manager.windows.forEach { win ->
                val isFocused = !win.isMinimized && win.zIndex == topZ
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(36.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when {
                                    isFocused -> Color(0x40FFFFFF)
                                    !win.isMinimized -> Color(0x22FFFFFF)
                                    else -> Color.Transparent
                                }
                            )
                            .clickable {
                                if (win.isMinimized) win.isMinimized = false
                                manager.bringToFront(win)
                            }
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = win.title, color = colors.taskbarText)
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .width(if (isFocused) 20.dp else 6.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(50))
                            .background(if (isFocused) Win11Blue else colors.taskbarText.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}
