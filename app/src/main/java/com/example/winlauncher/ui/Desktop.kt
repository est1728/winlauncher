package com.example.winlauncher.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.winlauncher.ui.theme.DesktopColors
import com.example.winlauncher.ui.theme.WallpaperPresets
import com.example.winlauncher.ui.theme.desktopColorsFor

/**
 * รากของ UI ทั้งหมด — เทียบเท่ากับ "หน้าจอ Windows" หลังล็อกอิน
 * คุมสถานะหลักของทั้งระบบไว้ที่นี่: วอลเปเปอร์ที่เลือก, โหมดมืด/สว่าง, หน้าต่างทั้งหมด
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DesktopScreen() {
    val manager = remember { DesktopWindowManager() }
    var showStartMenu by remember { mutableStateOf(false) }
    var showPersonalize by remember { mutableStateOf(false) }
    var showContextMenu by remember { mutableStateOf(false) }

    // ---------- ธีม + วอลเปเปอร์ (ปรับได้จากแผง Personalize) ----------
    var selectedWallpaper by remember { mutableStateOf(WallpaperPresets.first()) }
    var isDarkMode by remember { mutableStateOf(selectedWallpaper.isDark) }
    val colors = desktopColorsFor(isDarkMode)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val desktopWidth: Dp = maxWidth
        val desktopHeight: Dp = maxHeight - 52.dp // หัก taskbar ออก

        // ---------- พื้นหลัง (วอลเปเปอร์) ----------
        WallpaperBackground(option = selectedWallpaper, modifier = Modifier.fillMaxSize())

        // ---------- พื้นที่ว่างของเดสก์ท็อป: กดค้าง (นิ้ว) หรือคลิกขวา (เมาส์) เพื่อเปิดเมนู ----------
        Box(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { showStartMenu = false; showContextMenu = false },
                    onLongClick = { showContextMenu = true }, // กดค้างด้วยนิ้ว
                )
                .pointerInput(Unit) {
                    // รองรับคลิกขวาจริงถ้ามีเมาส์ต่ออยู่ (Android ส่ง secondary button event มาให้)
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                                showContextMenu = true
                            }
                        }
                    }
                }
        )

        // ---------- ไอคอนเดสก์ท็อป ----------
        DesktopIconsGrid(
            icons = DefaultDesktopIcons,
            textColor = Color.White,
            onOpen = { item -> manager.openWindow(id = item.id, title = item.label) },
        )

        // ---------- หน้าต่างทั้งหมดที่เปิดอยู่ ----------
        manager.windows.forEach { win ->
            DesktopWindow(
                state = win,
                manager = manager,
                desktopWidthDp = desktopWidth,
                desktopHeightDp = desktopHeight,
                colors = colors,
            ) {
                DemoWindowContent(title = win.title, textColor = colors.text)
            }
        }

        // ---------- เมนูคลิกขวา/กดค้างบนพื้นเดสก์ท็อป ----------
        if (showContextMenu) {
            DesktopContextMenu(
                onDismiss = { showContextMenu = false },
                onPersonalize = {
                    showContextMenu = false
                    showPersonalize = true
                },
            )
        }

        // ---------- แผง Personalize ----------
        if (showPersonalize) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(3000f)
                    .background(Color(0x66000000))
                    .pointerInput(Unit) { detectDismissTap { showPersonalize = false } },
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.pointerInput(Unit) { /* กันแตะทะลุไปปิดตอนแตะข้างในแผง */ }) {
                    PersonalizePanel(
                        currentWallpaper = selectedWallpaper,
                        isDarkMode = isDarkMode,
                        onWallpaperSelected = { selectedWallpaper = it },
                        onDarkModeChanged = { isDarkMode = it },
                        onClose = { showPersonalize = false },
                    )
                }
            }
        }

        // ---------- Start Menu แบบง่าย ----------
        if (showStartMenu) {
            SimpleStartMenu(
                colors = colors,
                onDismiss = { showStartMenu = false },
                onOpenApp = { appName ->
                    manager.openWindow(id = appName, title = appName)
                    showStartMenu = false
                }
            )
        }

        // ---------- Taskbar ด้านล่าง ----------
        Box(modifier = Modifier.align(Alignment.BottomCenter).zIndex(1000f)) {
            Taskbar(manager = manager, colors = colors, onStartClick = { showStartMenu = !showStartMenu })
        }
    }
}

/** ตรวจจับแตะเพื่อปิด overlay (ใช้กับพื้นหลังโปร่งของ dialog ต่างๆ) */
private suspend fun PointerInputScope.detectDismissTap(onDismiss: () -> Unit) {
    detectTapGestures { onDismiss() }
}

/** เมนูที่เด้งขึ้นมาตอนกดค้าง/คลิกขวาบนพื้นเดสก์ท็อป — คล้ายเมนู Personalize ของ Windows */
@Composable
private fun DesktopContextMenu(onDismiss: () -> Unit, onPersonalize: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().zIndex(2500f)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectDismissTap(onDismiss) }
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(vertical = 6.dp)
        ) {
            ContextMenuItem(label = "Personalize", onClick = { onDismiss(); onPersonalize() })
            ContextMenuItem(label = "Refresh", onClick = onDismiss)
        }
    }
}

@Composable
private fun ContextMenuItem(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    )
}

/** เนื้อหาตัวอย่างในหน้าต่าง ไว้ทดสอบว่าลาก/ย่อ/ขยายทำงานถูกต้อง */
@Composable
private fun DemoWindowContent(title: String, textColor: Color) {
    SelectionContainer {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "นี่คือหน้าต่าง: $title", style = MaterialTheme.typography.titleMedium, color = textColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ลองลากแถบหัวด้านบนเพื่อย้ายตำแหน่ง", color = textColor)
            Text(text = "ลองลากมุมขวาล่างเพื่อปรับขนาด", color = textColor)
            Text(text = "กดปุ่มมุมขวาบนเพื่อย่อ/ขยาย/ปิด", color = textColor)
            Text(text = "กดค้างพื้นเดสก์ท็อป (หรือคลิกขวาถ้ามีเมาส์) เพื่อเปลี่ยนวอลเปเปอร์/ธีม", color = textColor)
        }
    }
}

/** เมนู Start แบบง่าย — รายชื่อ "แอป" ตัวอย่างที่กดแล้วเปิดเป็นหน้าต่างใหม่ */
@Composable
private fun SimpleStartMenu(
    colors: DesktopColors,
    onDismiss: () -> Unit,
    onOpenApp: (String) -> Unit,
) {
    val demoApps = listOf("File Explorer", "Settings", "Notepad", "Calculator")

    Box(modifier = Modifier.fillMaxSize().zIndex(2000f)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectDismissTap(onDismiss) }
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 60.dp)
                .width(260.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.windowBody)
                .padding(12.dp)
        ) {
            Text(text = "แอปทั้งหมด", style = MaterialTheme.typography.titleSmall, color = colors.text)
            Spacer(modifier = Modifier.height(8.dp))
            demoApps.forEach { app ->
                Text(
                    text = app,
                    color = colors.text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(onClick = { onOpenApp(app) })
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}
