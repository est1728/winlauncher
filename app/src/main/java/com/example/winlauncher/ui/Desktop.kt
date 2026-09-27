package com.example.winlauncher.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.winlauncher.ui.theme.DesktopColors
import com.example.winlauncher.ui.theme.WallpaperPresets
import com.example.winlauncher.ui.theme.desktopColorsFor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DesktopScreen() {
    val manager = remember { DesktopWindowManager() }
    var showStartMenu by remember { mutableStateOf(false) }
    var showPersonalize by remember { mutableStateOf(false) }
    var showContextMenu by remember { mutableStateOf(false) }

    var selectedWallpaper by remember { mutableStateOf(WallpaperPresets.first()) }
    var isDarkMode by remember { mutableStateOf(selectedWallpaper.isDark) }
    val colors = desktopColorsFor(isDarkMode)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val desktopWidth: Dp = maxWidth
        val desktopHeight: Dp = maxHeight - 52.dp

        WallpaperBackground(option = selectedWallpaper, modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { showStartMenu = false; showContextMenu = false },
                    onLongClick = { showContextMenu = true },
                )
                .pointerInput(Unit) {
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

        DesktopIconsGrid(
            icons = DefaultDesktopIcons,
            textColor = Color.White,
            onOpen = { item -> manager.openWindow(id = item.id, title = item.label) },
        )

        manager.windows.forEach { win ->
            DesktopWindow(
                state = win,
                manager = manager,
                desktopWidthDp = desktopWidth,
                desktopHeightDp = desktopHeight,
                colors = colors,
            ) {
                when (win.id) {
                    "File Explorer" -> FileExplorerApp(textColor = colors.text)
                    else -> DemoWindowContent(title = win.title, textColor = colors.text)
                }
            }
        }

        if (showContextMenu) {
            DesktopContextMenu(
                onDismiss = { showContextMenu = false },
                onPersonalize = {
                    showContextMenu = false
                    showPersonalize = true
                },
            )
        }

        if (showPersonalize) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(3000f)
                    .background(Color(0x66000000))
                    .pointerInput(Unit) { detectDismissTap { showPersonalize = false } },
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.pointerInput(Unit) { }) {
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

        if (showStartMenu) {
            RealStartMenu(
                colors = colors,
                onDismiss = { showStartMenu = false },
            )
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).zIndex(1000f)) {
            Taskbar(manager = manager, colors = colors, onStartClick = { showStartMenu = !showStartMenu })
        }
    }
}

private suspend fun PointerInputScope.detectDismissTap(onDismiss: () -> Unit) {
    detectTapGestures { onDismiss() }
}

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

@Composable
private fun DemoWindowContent(title: String, textColor: Color) {
    SelectionContainer {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "นี่คือหน้าต่าง: $title", style = MaterialTheme.typography.titleMedium, color = textColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ลองลากแถบหัวด้านบนเพื่อย้ายตำแหน่ง", color = textColor)
            Text(text = "ลองลากมุมขวาล่างเพื่อปรับขนาด", color = textColor)
            Text(text = "กดปุ่มมุมขวาบนเพื่อย่อ/ขยาย/ปิด", color = textColor)
        }
    }
}

@Composable
private fun RealStartMenu(
    colors: DesktopColors,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val apps = remember { loadInstalledApps(context) }

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
                .width(320.dp)
                .heightIn(max = 420.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.windowBody)
                .padding(12.dp)
        ) {
            Text(
                text = "แอปทั้งหมด (${apps.size})",
                style = MaterialTheme.typography.titleSmall,
                color = colors.text,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                items(apps) { app ->
                    Column(
                        modifier = Modifier
                            .clickable {
                                launchApp(context, app.packageName)
                                onDismiss()
                            }
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            bitmap = app.icon,
                            contentDescription = app.label,
                            modifier = Modifier.size(40.dp),
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = app.label,
                            color = colors.text,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
