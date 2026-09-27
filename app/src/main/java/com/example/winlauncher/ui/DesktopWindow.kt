package com.example.winlauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Crop169
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.example.winlauncher.ui.theme.DesktopColors

/**
 * หน้าต่างหนึ่งบานบนเดสก์ท็อป
 * - แถบหัว (title bar) ลากด้วยนิ้ว/เมาส์เพื่อย้ายตำแหน่งได้
 * - มุมขวาล่างลากเพื่อปรับขนาดได้
 * - ปุ่ม _ / □ / ✕ มุมขวาบน ทำงานเหมือน Windows จริง
 */
@Composable
fun DesktopWindow(
    state: WindowState,
    manager: DesktopWindowManager,
    desktopWidthDp: androidx.compose.ui.unit.Dp,
    desktopHeightDp: androidx.compose.ui.unit.Dp,
    colors: DesktopColors,
    content: @Composable () -> Unit,
) {
    if (state.isMinimized) return // หน้าต่างที่ย่อไว้จะไม่วาดบนเดสก์ท็อป (ไปอยู่ที่ taskbar แทน)

    Box(
        modifier = Modifier
            .offset(x = state.offsetX, y = state.offsetY)
            .size(width = state.width, height = state.height)
            .zIndex(state.zIndex)
            .clip(RoundedCornerShape(if (state.isMaximized) 0.dp else 8.dp))
            .background(colors.windowBody)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ---------- แถบหัวหน้าต่าง (Title bar) ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(colors.windowTitleBar)
                    .pointerInput(state.id) {
                        detectDragGestures(
                            onDragStart = { manager.bringToFront(state) }
                        ) { change, dragAmount ->
                            change.consume()
                            if (!state.isMaximized) {
                                state.offsetX += with(this) { dragAmount.x.toDp() }
                                state.offsetY += with(this) { dragAmount.y.toDp() }
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.title,
                    fontWeight = FontWeight.Medium,
                    color = colors.text,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )

                // ปุ่ม _ (minimize)
                IconButton(onClick = { manager.minimize(state) }, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Filled.Minimize, contentDescription = "ย่อหน้าต่าง")
                }
                // ปุ่ม □ (maximize / restore)
                IconButton(
                    onClick = { manager.toggleMaximize(state, desktopWidthDp, desktopHeightDp) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Filled.Crop169, contentDescription = "ขยาย/คืนขนาดหน้าต่าง")
                }
                // ปุ่ม ✕ (close)
                IconButton(
                    onClick = { manager.closeWindow(state) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "ปิดหน้าต่าง",
                        tint = com.example.winlauncher.ui.theme.Win11CloseRed
                    )
                }
            }

            // ---------- เนื้อหาข้างในหน้าต่าง ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(state.id) {
                        // แตะที่เนื้อหาก็ต้องยกหน้าต่างขึ้นมาบนสุดด้วย
                        detectDragGestures(onDragStart = { manager.bringToFront(state) }) { _, _ -> }
                    }
            ) {
                content()
            }
        }

        // ---------- จุดลากปรับขนาด มุมขวาล่าง ----------
        if (!state.isMaximized) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(24.dp)
                    .pointerInput(state.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val newWidth = state.width + with(this) { dragAmount.x.toDp() }
                            val newHeight = state.height + with(this) { dragAmount.y.toDp() }
                            if (newWidth > state.minWidth) state.width = newWidth
                            if (newHeight > state.minHeight) state.height = newHeight
                        }
                    }
                    .background(Color.Transparent)
            )
        }
    }
}
