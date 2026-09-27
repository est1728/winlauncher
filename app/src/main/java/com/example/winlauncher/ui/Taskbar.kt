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

/**
 * แถบ Taskbar แบบ Windows 11 — อยู่กึ่งกลางด้านล่างจอ
 * แสดงปุ่ม Start (ตรงกลาง) + ไอคอนหน้าต่างที่เปิดอยู่ทุกบาน (รวมที่ย่อไว้)
 */
@Composable
fun Taskbar(
    manager: DesktopWindowManager,
    colors: DesktopColors,
    onStartClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(colors.taskbar),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            // ปุ่ม Start
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onStartClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Apps, contentDescription = "Start", tint = colors.taskbarText)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // ไอคอนหน้าต่างที่เปิดอยู่ (ทั้งที่แสดงและที่ย่อไว้)
            manager.windows.forEach { win ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (win.isMinimized) Color.Transparent else Color(0x33FFFFFF))
                        .clickable {
                            if (win.isMinimized) win.isMinimized = false
                            manager.bringToFront(win)
                        }
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = win.title, color = colors.taskbarText)
                }
            }
        }
    }
}
