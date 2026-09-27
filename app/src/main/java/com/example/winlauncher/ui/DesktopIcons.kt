package com.example.winlauncher.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** รายการไอคอนเดสก์ท็อป — ตอนนี้เป็นแอปตัวอย่าง เชื่อมกับหน้าต่างจริงที่ WindowManager เปิดได้แล้ว */
data class DesktopIconItem(val id: String, val label: String, val icon: ImageVector)

val DefaultDesktopIcons = listOf(
    DesktopIconItem("File Explorer", "File Explorer", Icons.Filled.Folder),
    DesktopIconItem("Settings", "Settings", Icons.Filled.Settings),
    DesktopIconItem("Notepad", "Notepad", Icons.Filled.Description),
    DesktopIconItem("Calculator", "Calculator", Icons.Filled.Calculate),
)

@Composable
fun DesktopIconsGrid(
    icons: List<DesktopIconItem>,
    textColor: Color,
    onOpen: (DesktopIconItem) -> Unit,
) {
    // จัดเป็นคอลัมน์เดียวชิดซ้ายบน แบบไอคอนเดสก์ท็อป Windows ทั่วไป
    Column(
        modifier = Modifier.padding(top = 16.dp, start = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        icons.forEach { item ->
            DesktopIconCell(item = item, textColor = textColor, onOpen = { onOpen(item) })
        }
    }
}

@Composable
private fun DesktopIconCell(item: DesktopIconItem, textColor: Color, onOpen: () -> Unit) {
    Column(
        modifier = Modifier
            .width(76.dp)
            .combinedClickable(
                interactionSource = remember_ { },
                indication = null,
                onClick = {},          // แตะครั้งเดียว = แค่เลือก (ยังไม่เปิด) เหมือน Windows จริง
                onDoubleClick = onOpen, // แตะสองครั้ง = เปิดจริง
            )
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(item.icon, contentDescription = item.label, tint = Color.White, modifier = Modifier.size(36.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.label,
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
    }
}

// helper เล็กๆ เพราะ remember ต้องเรียกใน @Composable scope ปกติ — เขียนแบบ inline ให้ compile ผ่านตรงๆ
@Composable
private fun remember_(): MutableInteractionSource =
    androidx.compose.runtime.remember { MutableInteractionSource() }
